package com.rkc.zds.controller;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkc.zds.api.exception.InvalidRequestException;
import com.rkc.zds.api.exception.NoAuthorizationException;
import com.rkc.zds.api.exception.ResourceNotFoundException;
import com.rkc.zds.core.service.AuthorizationService;
import com.rkc.zds.model.CommentData;
import com.rkc.zds.dto.ArticleCommentDto;
import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.repository.ArticleRepository;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.repository.ArticleCommentRepository;
import com.rkc.zds.service.CommentQueryService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
//@RequestMapping(path = "/articles/{slug}/comments")
public class CommentsApi {
	private ArticleRepository articleRepository;
	private ArticleCommentRepository commentRepository;
	private CommentQueryService commentQueryService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	public CommentsApi(ArticleRepository articleRepository, ArticleCommentRepository commentRepository,
			CommentQueryService commentQueryService) {
		this.articleRepository = articleRepository;
		this.commentRepository = commentRepository;
		this.commentQueryService = commentQueryService;
	}

	/*
	 * @PostMapping public ResponseEntity<?> createComment(@PathVariable("slug")
	 * String slug,
	 * 
	 * @AuthenticationPrincipal UserDto user,
	 * 
	 * @Valid @RequestBody NewCommentParam newCommentParam, BindingResult
	 * bindingResult) {
	 * 
	 */
	@RequestMapping(value = "/api/articles/{id}/comments", method = RequestMethod.POST, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })

	// public ResponseEntity<?> createComment(@PathVariable("slug") String slug,
	// @RequestBody NewCommentParam newCommentParam) {
	public ResponseEntity<?> createComment(@PathVariable("id") Integer id, @RequestBody String jsonString) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}

		ArticleDto article = findArticle(id);

		ArticleCommentDto comment = new ArticleCommentDto();

		ObjectMapper mapper = new ObjectMapper();

		comment.setUserId(user.getId());

		comment.setArticleId(article.getId());

		comment.setBody(jsonString);

		Timestamp stamp = new Timestamp(new Date().getTime());
		comment.setCreatedAt(stamp);
		
		comment = commentRepository.save(comment);
//		return ResponseEntity.status(201)
//				.body(commentQueryService.findByArticleIdAndUserId(article.getId(), user.getId()).get());

		//return ResponseEntity.status(201)
		//.body(commentQueryService.findById(comment.getId(), user).get());
		
        return ResponseEntity.status(201).body(commentResponse(commentQueryService.findById(comment.getId(), user).get()));
	
	}

	@RequestMapping(value = "/api/articles/{articleId}/comments", method = RequestMethod.GET,  produces = { "application/json;charset=UTF-8" })
//	public ResponseEntity getComments(@PathVariable("slug") String slug) {
	public ResponseEntity getComments(@PathVariable("articleId") Integer articleId) {
		
		ArticleDto article = findArticle(articleId);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}

		List<CommentData> comments = commentQueryService.findByArticleId(article.getId(), user);
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("comments", comments);
			}
		});
	}

	@RequestMapping(path = "/api/articles/{articleId}/comments/{commentId}", method = RequestMethod.DELETE)
	public ResponseEntity deleteComment(@PathVariable("articleId") Integer articleId, @PathVariable("commentId") Integer commentId) {
		ArticleDto article = findArticle(articleId);
			
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userLogin = authentication.getName();

		Optional<UserDto> userDto = userRepository.findByUserName(userLogin);
		
		UserDto user = null;
		if(userDto.isPresent()) {
			user = userDto.get();
		}
		
		final UserDto userTemp = user;
		
		return commentRepository.findByArticleIdAndId(article.getId(), commentId).map(comment -> {
			if (!AuthorizationService.canWriteComment(userTemp, article, comment)) {
				throw new NoAuthorizationException();
			}
			commentRepository.delete(comment);
			return ResponseEntity.noContent().build();
		}).orElseThrow(ResourceNotFoundException::new);
	}

	private ArticleDto findArticle(Integer id) {
		return articleRepository.findById(id).map(article -> article).orElseThrow(ResourceNotFoundException::new);
	}

	private Map<String, Object> commentResponse(CommentData commentData) {
		return new HashMap<String, Object>() {
			{
				put("comment", commentData);
			}
		};
	}
}
/*
 * @Getter
 * 
 * @NoArgsConstructor
 * 
 * @JsonRootName("comment") class NewCommentParam {
 * 
 * @NotBlank(message = "can't be empty") private String body; }
 */
