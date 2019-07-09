package com.rkc.zds.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.dto.ArticleCommentDto;
import com.rkc.zds.dto.ArticleFavoriteDto;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavoriteDto, Integer> {
	ArticleFavoriteDto save(ArticleFavoriteDto articleFavorite);

	Optional<ArticleFavoriteDto> findByArticleIdAndUserId(Integer articleId, Integer userId);
	
	List<ArticleFavoriteDto> findByArticleId(Integer articleId);

	List<ArticleFavoriteDto> findByUserId(Integer id);
}
