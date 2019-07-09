package com.rkc.zds.service;

import com.rkc.zds.dto.ArticleCommentDto;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.model.CommentData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Mapper
@Service
public interface CommentReadService {
    CommentData findById(@Param("id") Integer id, UserDto user);

    List<CommentData> findByArticleId(@Param("articleId") Integer articleId);

    Optional<ArticleCommentDto> findByArticleIdAndUserId(Integer articleId, Integer userId);
}
