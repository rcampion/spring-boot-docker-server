package com.rkc.zds.service;

import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.dto.ContactDto;
import com.rkc.zds.model.ArticleData;
import com.rkc.zds.model.ArticleDataList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Mapper

public interface ArticleReadService {
    ArticleData findById(@Param("id") Integer id);

    ArticleData findBySlug(@Param("slug") String slug);

    List<String> queryArticles(Pageable pageable, @Param("tag") String tag, @Param("author") String author, @Param("favoritedBy") String favoritedBy);

    int countArticle(@Param("tag") String tag, @Param("author") String author, @Param("favoritedBy") String favoritedBy);

    List<ArticleData> findArticles(Pageable pageable, @Param("articleIds") List<String> articleIds);

    ArticleDataList findArticlesOfAuthors(Pageable pageable, @Param("authors") List<Integer> authors);

    int countFeedSize(@Param("authors") List<Integer> authors);

	Page<ArticleDto> findAll(Pageable pageable);

	Page<ArticleDto> findByUserId(Pageable pageable, Integer id);
		
	Page<ArticleDto> searchArticles(Pageable pageable, Specification<ArticleDto> spec);

	Page<ArticleDto> findFavorites(Pageable pageable, Integer id);
	
	Page<ArticleDto> findByTag(Pageable pageable, String tag);

}
