package com.rkc.zds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rkc.zds.api.exception.NoAuthorizationException;
import com.rkc.zds.api.exception.ResourceNotFoundException;
import com.rkc.zds.core.service.AuthorizationService;
import com.rkc.zds.dto.ArticleTagArticleDto;
import com.rkc.zds.dto.ArticleTagDto;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.repository.ArticleRepository;
import com.rkc.zds.repository.ArticleTagArticleRepository;
import com.rkc.zds.repository.ArticleTagRepository;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.service.TagsQueryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
@RequestMapping(path = "/api/tags")
public class TagsApi {
		
	@Autowired
	UserRepository userRepository;

	@Autowired
	ArticleTagRepository tagRepository;
	
	@Autowired
	ArticleTagArticleRepository tagArticleRepository;
	
	@Autowired	
	ArticleRepository articleRepository;
	
    private TagsQueryService tagsQueryService;

    @Autowired
    public TagsApi(TagsQueryService tagsQueryService) {
        this.tagsQueryService = tagsQueryService;
    }

    @GetMapping
    public ResponseEntity getTags() {
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("tags", tagsQueryService.allTags());
        }});
    }
    
	@RequestMapping(value = "{tag:.+}/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity deleteTag(@PathVariable("tag") String tag, @PathVariable("id") Integer id) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}
		
		final UserDto temp = user;
		
		
		ArticleTagDto tagDto = tagRepository.findByName(tag);
		
		ArticleTagArticleDto tagArticle = tagArticleRepository.findByTagIdAndArticleId(tagDto.getId(), id);
		
		//return articleRepository.findBySlug(slug).map(article -> {
		return articleRepository.findById(id).map(article -> {
			if (!AuthorizationService.canWriteArticle(temp, article)) {
				throw new NoAuthorizationException();
			}
			tagArticleRepository.delete(tagArticle);
			
			//if there are no articles with this tag, delete tag
			List<ArticleTagArticleDto> tagArticleList = new ArrayList<ArticleTagArticleDto>();
			tagArticleList = tagArticleRepository.findByTagId(tagDto.getId());
			if(tagArticleList.size()==0) {
				tagRepository.delete(tagDto);
			}
			
			return ResponseEntity.noContent().build();
		}).orElseThrow(ResourceNotFoundException::new);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity deleteTagX(@PathVariable("id") Integer id) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}
		
		final UserDto temp = user;
		
		String tag = "";
		
		ArticleTagDto tagDto = tagRepository.findByName(tag);
		
		ArticleTagArticleDto tagArticle = tagArticleRepository.findByTagIdAndArticleId(tagDto.getId(), id);
		
		//return articleRepository.findBySlug(slug).map(article -> {
		return articleRepository.findById(id).map(article -> {
			if (!AuthorizationService.canWriteArticle(temp, article)) {
				throw new NoAuthorizationException();
			}
			tagArticleRepository.delete(tagArticle);
			
			//if there are no articles with this tag, delete tag
			List<ArticleTagArticleDto> tagArticleList = new ArrayList<ArticleTagArticleDto>();
			tagArticleList = tagArticleRepository.findByTagId(tagDto.getId());
			if(tagArticleList.size()==0) {
				tagRepository.delete(tagDto);
			}
			return ResponseEntity.noContent().build();
		}).orElseThrow(ResourceNotFoundException::new);
	}
}
