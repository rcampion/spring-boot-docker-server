package com.rkc.zds.service;

import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.core.article.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {
    void insert(@Param("article") ArticleDto article);

    ArticleDto findById(@Param("id") String id);

    boolean findTag(@Param("tagName") String tagName);

    void insertTag(@Param("tag") Tag tag);

    void insertArticleTagRelation(@Param("articleId") String articleId, @Param("tagId") String tagId);

    ArticleDto findBySlug(@Param("slug") String slug);

    void update(@Param("article") ArticleDto article);

    void delete(@Param("id") String id);
}
