package com.rkc.zds.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rkc.zds.dto.ArticleCommentDto;

@Mapper
public interface CommentMapper {
    void insert(@Param("comment") ArticleCommentDto comment);

    ArticleCommentDto findById(@Param("articleId") String articleId, @Param("id") String id);

    void delete(@Param("id") String id);
}
