package com.rkc.zds.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rkc.zds.dto.ArticleFavoriteDto;

@Mapper
public interface ArticleFavoriteMapper {
    ArticleFavoriteDto find(@Param("articleId") String articleId, @Param("userId") Integer integer);

    void insert(@Param("articleFavorite") ArticleFavoriteDto articleFavorite);

    void delete(@Param("favorite") ArticleFavoriteDto favorite);
}
