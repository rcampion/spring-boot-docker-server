package com.rkc.zds.service;

import com.rkc.zds.model.ArticleData;
import com.rkc.zds.model.ArticleDataList;
import com.rkc.zds.model.ArticleFavoriteCount;
import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

//@Component
@Service
@Qualifier("articleQueryService")
public class ArticleQueryService {

	// @Autowired
	// @Qualifier(value="articleReadService")
	private ArticleReadService articleReadService;
	// @Autowired
	private UserRelationshipQueryService userRelationshipQueryService;
	// @Autowired
	private ArticleFavoritesReadService articleFavoritesReadService;

	@Autowired
	public ArticleQueryService(ArticleReadService articleReadService,
			UserRelationshipQueryService userRelationshipQueryService,
			ArticleFavoritesReadService articleFavoritesReadService) {
		this.articleReadService = articleReadService;
		this.userRelationshipQueryService = userRelationshipQueryService;
		this.articleFavoritesReadService = articleFavoritesReadService;
	}

	public Optional<ArticleData> findById(Integer articleId, UserDto user) {
		ArticleData articleData = articleReadService.findById(articleId);
		if (articleData == null) {
			return Optional.empty();
		} else {
			if (user != null) {
				fillExtraInfo(articleId, user, articleData);
			}
			return Optional.of(articleData);
		}
	}

	public Optional<ArticleData> findBySlug(String slug, UserDto user) {
		ArticleData articleData = articleReadService.findBySlug(slug);
		if (articleData == null) {
			return Optional.empty();
		} else {
			if (user != null) {
				fillExtraInfo(articleData.getId(), user, articleData);
			}
			return Optional.of(articleData);
		}
	}

	public ArticleDataList findRecentArticles(Pageable pageable, String tag, String author, String favoritedBy,
			UserDto currentUser) {
		List<String> articleIds = articleReadService.queryArticles(pageable, tag, author, favoritedBy);
		// int articleCount = articleReadService.countArticle(tag, author, favoritedBy);
		int articleCount = articleIds.size();
		if (articleIds.size() == 0) {
			return new ArticleDataList(new ArrayList<>(), articleCount);
		} else {
			List<ArticleData> articles = articleReadService.findArticles(pageable, articleIds);
			fillExtraInfo(articles, currentUser);
			return new ArticleDataList(articles, articleCount);
		}
	}

	private void fillExtraInfo(List<ArticleData> articles, UserDto currentUser) {
		setFavoriteCount(articles);
		if (currentUser != null) {
			setIsFavorite(articles, currentUser);
			setIsFollowingAuthor(articles, currentUser);
		}
	}

	private void setIsFollowingAuthor(List<ArticleData> articles, UserDto currentUser) {
		Set<Integer> followingAuthors = userRelationshipQueryService.followingAuthors(currentUser.getId(),
				articles.stream().map(articleData1 -> articleData1.getProfileData().getId()).collect(toList()));
		articles.forEach(articleData -> {
			if (followingAuthors.contains(articleData.getProfileData().getId())) {
				articleData.getProfileData().setFollowing(true);
			}
		});
	}

	private void setFavoriteCount(List<ArticleData> articles) {
		List<ArticleFavoriteCount> favoritesCounts = articleFavoritesReadService
				.articlesFavoriteCount(articles.stream().map(ArticleData::getId).collect(toList()));
		Map<Integer, Integer> countMap = new HashMap<>();
		favoritesCounts.forEach(item -> {
			countMap.put(item.getId(), item.getCount());
		});
//        articles.forEach(articleData -> articleData.setFavoritesCount(countMap.get(articleData.getId())));

		for (ArticleData articleData : articles) {
			Integer test = countMap.get(articleData.getId());
			articleData.setFavoritesCount(countMap.get(articleData.getId()));

		}

	}

	private void setIsFavorite(List<ArticleData> articles, UserDto currentUser) {
		Set<Integer> favoritedArticles = articleFavoritesReadService.userFavorites(
				articles.stream().map(articleData -> articleData.getId()).collect(toList()), currentUser);

		if (!favoritedArticles.isEmpty()) {
			articles.forEach(articleData -> {
				if (favoritedArticles.contains(articleData.getId())) {
					articleData.setFavorited(true);
				}
			});
		}
	}

	private void fillExtraInfo(Integer articleId, UserDto user, ArticleData articleData) {
		articleData.setFavorited(articleFavoritesReadService.isUserFavorite(user.getId(), articleId));
		articleData.setFavoritesCount(articleFavoritesReadService.articleFavoriteCount(articleId));
		articleData.getProfileData().setFollowing(
				userRelationshipQueryService.isUserFollowing(user.getId(), articleData.getProfileData().getId()));
	}

	public ArticleDataList findUserFeed(Pageable pageable, UserDto user) {
		List<Integer> followedUsers = userRelationshipQueryService.followedUsers(user.getId());
		if (followedUsers.size() == 0) {
			return new ArticleDataList(new ArrayList<>(), 0);
		} else {
			ArticleDataList articles = articleReadService.findArticlesOfAuthors(pageable, followedUsers);
			fillExtraInfo(articles.getList(), user);
			//int count = articleReadService.countFeedSize(followedUsers);
			return new ArticleDataList(articles.getList(), articles.getCount());
		}
	}

	public Page<ArticleDto> findArticles(Pageable pageable, String tag, String author, String favoritedBy, UserDto userDto) {
		
		return articleReadService.findAll(pageable);
	}
}
