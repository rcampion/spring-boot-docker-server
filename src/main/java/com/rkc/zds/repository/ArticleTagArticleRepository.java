package com.rkc.zds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.dto.ArticleTagArticleDto;

public interface ArticleTagArticleRepository extends JpaRepository<ArticleTagArticleDto, Integer>, JpaSpecificationExecutor<ArticleTagArticleDto> {

	List<ArticleTagArticleDto> findByTagId(Integer id);

	ArticleTagArticleDto findByTagIdAndArticleId(Integer tagId, Integer articleId);

	List<ArticleTagArticleDto> findByArticleId(Integer articleId);


}
