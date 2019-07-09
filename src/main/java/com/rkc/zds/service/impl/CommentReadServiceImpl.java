package com.rkc.zds.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkc.zds.dto.ArticleCommentDto;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.model.CommentData;
import com.rkc.zds.model.ProfileData;
import com.rkc.zds.repository.ArticleCommentRepository;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.service.CommentReadService;

@Service
public class CommentReadServiceImpl implements CommentReadService {

	@Autowired
	ArticleCommentRepository articleCommentRepo;

	@Autowired
	UserRepository userRepo;
	
	@Override
	public CommentData findById(Integer id, UserDto userDto) {
		
		Optional<ArticleCommentDto> element = articleCommentRepo.findById(id);
		CommentData data = null;
		Optional<UserDto> user = null;
		ProfileData profile = null;
		
		if(element.isPresent()) {
			ArticleCommentDto comment = element.get();
			data = new CommentData();
			data.setArticleId(comment.getArticleId());
			data.setBody(comment.getBody());
			data.setCreatedAt(comment.getCreatedAt());
						
			user = userRepo.findById(userDto.getId());
			if(user.isPresent()) {
				userDto = user.get();
				profile = new ProfileData();
				profile.setBio(userDto.getBio());
				profile.setFollowing(false);
				profile.setUserName(userDto.getUserName());
				
				data.setProfileData(profile);
			}			
		}
		
		return data;
	}

	@Override
	public List<CommentData> findByArticleId(Integer articleId) {
		List<ArticleCommentDto> articleCommentList = articleCommentRepo.findByArticleId(articleId);

		List<CommentData> commentDataList = new ArrayList<CommentData>();
		
		CommentData data = null;
		ProfileData profile = null;
		Optional<UserDto> user = null;
		UserDto userDto = null;
		
		for(ArticleCommentDto element: articleCommentList) {
			data = new CommentData();
			data.setId(element.getId());
			data.setArticleId(element.getArticleId());
			data.setBody(element.getBody());
			data.setCreatedAt(element.getCreatedAt());
						
			user = userRepo.findById(element.getUserId());
			if(user.isPresent()) {
				userDto = user.get();
				profile = new ProfileData();
				profile.setBio(userDto.getBio());
				profile.setFollowing(false);
				profile.setUserName(userDto.getUserName());
				
				data.setProfileData(profile);
			}
			
			commentDataList.add(data);
		}
		return commentDataList;
	}

	@Override
	public Optional<ArticleCommentDto> findByArticleIdAndUserId(Integer articleId, Integer userId) {

		Optional<ArticleCommentDto> comment = articleCommentRepo.findByArticleIdAndUserId(articleId, userId);

		return comment;
		
	}


}
