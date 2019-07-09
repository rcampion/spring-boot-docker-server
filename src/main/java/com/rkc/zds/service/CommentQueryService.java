package com.rkc.zds.service;

import com.rkc.zds.model.CommentData;
import com.rkc.zds.dto.ArticleCommentDto;
import com.rkc.zds.dto.UserDto;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentQueryService {
    private CommentReadService commentReadService;
    private UserRelationshipQueryService userRelationshipQueryService;

    public CommentQueryService(CommentReadService commentReadService, UserRelationshipQueryService userRelationshipQueryService) {
        this.commentReadService = commentReadService;
        this.userRelationshipQueryService = userRelationshipQueryService;
    }

    public Optional<CommentData> findById(Integer id, UserDto user) {
        CommentData commentData = commentReadService.findById(id, user);
        if (commentData == null) {
            return Optional.empty();
        } else {
            commentData.getProfileData().setFollowing(
                userRelationshipQueryService.isUserFollowing(
                    user.getId(),
                    commentData.getProfileData().getId()));
        }
        return Optional.ofNullable(commentData);
    }

    public List<CommentData> findByArticleId(Integer articleId, UserDto user) {
        List<CommentData> comments = commentReadService.findByArticleId(articleId);
/*
        if (comments.size() > 0 && user != null) {
            Set<String> followingAuthors = userRelationshipQueryService.followingAuthors(user.getId(), comments.stream().map(commentData -> commentData.getProfileData().getId()).collect(Collectors.toList()));
            comments.forEach(commentData -> {
                if (followingAuthors.contains(commentData.getProfileData().getId())) {
                    commentData.getProfileData().setFollowing(true);
                }
            });
        }
*/
        return comments;
    }

	public Optional<ArticleCommentDto> findByArticleIdAndUserId(Integer articleId , Integer userId) {
		return commentReadService.findByArticleIdAndUserId(articleId, userId);
	}
}
