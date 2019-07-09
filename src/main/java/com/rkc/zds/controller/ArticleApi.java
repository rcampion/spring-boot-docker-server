package com.rkc.zds.controller;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkc.zds.api.exception.NoAuthorizationException;
import com.rkc.zds.api.exception.ResourceNotFoundException;
import com.rkc.zds.core.service.AuthorizationService;
import com.rkc.zds.model.ArticleData;
import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.dto.ArticleTagDto;
import com.rkc.zds.dto.ArticleTagArticleDto;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.repository.ArticleRepository;
import com.rkc.zds.repository.ArticleTagArticleRepository;
import com.rkc.zds.repository.ArticleTagRepository;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.service.ArticleQueryService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
//@RequestMapping(path = "/api/articles/{slug}")
@RequestMapping(path = "/api/articles/")
public class ArticleApi {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	ArticleTagRepository tagRepository;
	
	@Autowired
	ArticleTagArticleRepository tagArticleRepository;
	
	private ArticleQueryService articleQueryService;
	private ArticleRepository articleRepository;

	@Autowired
	public ArticleApi(ArticleQueryService articleQueryService, ArticleRepository articleRepository) {
		this.articleQueryService = articleQueryService;
		this.articleRepository = articleRepository;
	}
	//@RequestMapping(path = "/api/articles/{slug}")
	//@GetMapping
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> article(@PathVariable("id") Integer id) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}

//		return articleQueryService.findBySlug(slug, userDto)
		return articleQueryService.findById(id, user)
				.map(articleData -> ResponseEntity.ok(articleResponse(articleData)))
				.orElseThrow(ResourceNotFoundException::new);
	}

	// @PutMapping
	//puts are not working, 415 error, switched to post
	@RequestMapping(value = "{id}", method = RequestMethod.POST, consumes = {
			"application/json" }, produces = { "application/json" })
	public ResponseEntity<?> updateArticle(@RequestBody String jsonString) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}

		ArticleDto article = new ArticleDto();

		ObjectMapper mapper = new ObjectMapper();

		try {
			article = mapper.readValue(jsonString, ArticleDto.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(article.getTags()!=null) {
			processTags(article);
		}
		
		Optional<ArticleDto> articleTemp = articleRepository.findById(article.getId());

		ArticleDto articleDto = null;
		if (articleTemp.isPresent()) {
			articleDto = articleTemp.get();
			
			articleDto.setBody(article.getBody());
			articleDto.setTitle(article.getTitle());
			articleDto.setDescription(article.getDescription());			
			Timestamp stamp = new Timestamp(new Date().getTime());

			articleDto.setUpdatedAt(stamp);
			articleDto = articleRepository.save(articleDto);
		}
		
		final Integer articleId = article.getId();
		
        return ResponseEntity.ok(articleResponse(articleQueryService.findById(articleId, user).get()));

	}

	private void processTags(ArticleDto article) {
		
		String tags = article.getTags();
		String[] array = tags.split("\\s+");
		ArticleTagDto tagDto = null;
		ArticleTagArticleDto tagArticleDto = null;
		
		List<ArticleTagArticleDto> articleTagList = null;
		
		for(String tag:array) {
			tagDto = tagRepository.findByName(tag);
			if(tagDto==null) {
				tagDto = new ArticleTagDto();
				tagDto.setName(tag);
				tagDto = tagRepository.save(tagDto);
			}
			if(tagDto!=null) {
				tagArticleDto = tagArticleRepository.findByTagIdAndArticleId(tagDto.getId(), article.getId());
				if(tagArticleDto == null) {
					tagArticleDto = new ArticleTagArticleDto();
					tagArticleDto.setTagId(tagDto.getId());
					tagArticleDto.setArticleId(article.getId());
					tagArticleDto = tagArticleRepository.save(tagArticleDto);
					
					
				}
			}
			
		}
		
		
	}
	
	//@DeleteMapping
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity deleteArticle(@PathVariable("id") Integer id) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}
		
		
		final UserDto temp = user;
		
		//return articleRepository.findBySlug(slug).map(article -> {
		return articleRepository.findById(id).map(article -> {
			if (!AuthorizationService.canWriteArticle(temp, article)) {
				throw new NoAuthorizationException();
			}
			articleRepository.delete(article);
			return ResponseEntity.noContent().build();
		}).orElseThrow(ResourceNotFoundException::new);
	}

	private Map<String, Object> articleResponse(ArticleData articleData) {
		return new HashMap<String, Object>() {
			{
				put("article", articleData);
			}
		};
	}
}
/*
 * @Getter
 * 
 * @NoArgsConstructor
 * 
 * @JsonRootName("article") class UpdateArticleParam { private String title =
 * ""; private String body = ""; private String description = ""; }
 */
