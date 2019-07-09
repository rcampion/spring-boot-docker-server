package com.rkc.zds.service;

import com.rkc.zds.model.ArticleFavoriteCount;
import com.rkc.zds.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ArticleFavoritesReadService {
    boolean isUserFavorite(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    int articleFavoriteCount(@Param("articleId") Integer articleId);

    List<ArticleFavoriteCount> articlesFavoriteCount(@Param("ids") List<Integer> list);

    Set<Integer> userFavorites(@Param("ids") List<Integer> list, @Param("currentUser") UserDto currentUser);
}
