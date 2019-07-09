package com.rkc.zds.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.dto.ArticleFavoriteDto;
import com.rkc.zds.dto.ArticleTagDto;
import com.rkc.zds.dto.ArticleTagArticleDto;
import com.rkc.zds.dto.UserDto;
import com.rkc.zds.model.ArticleData;
import com.rkc.zds.model.ProfileData;
import com.rkc.zds.repository.ArticleFavoriteRepository;
import com.rkc.zds.repository.ArticleRepository;
import com.rkc.zds.repository.ArticleTagArticleRepository;
import com.rkc.zds.repository.ArticleTagRepository;
import com.rkc.zds.repository.UserRepository;
import com.rkc.zds.rsql.CustomRsqlVisitor;
import com.rkc.zds.service.ArticleReadService;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

@Service("articleReadService")
@Qualifier("articleReadService")
public class ArticleReadServiceImpl implements ArticleReadService {

	@Autowired
	private ArticleRepository articleRepo;

	@Autowired
	private ArticleTagRepository articleTagRepo;

	@Autowired
	private ArticleTagArticleRepository articleTagArticleRepo;

	@Autowired
	private ArticleFavoriteRepository articleFavoriteRepo;
	
	@Autowired
	private UserRepository userRepo;

	@Override
	public ArticleData findById(Integer id) {
		Optional<ArticleDto> article = articleRepo.findById(id);

		ArticleData data = null;
		ProfileData profile = null;
		ArticleDto articleDto = null;

		if (article.isPresent()) {
			articleDto = article.get();
			data = new ArticleData();

			data.setId(articleDto.getId());
			data.setBody(articleDto.getBody());
			data.setTitle(articleDto.getTitle());
			data.setCreatedAt(articleDto.getCreatedAt());
			data.setUpdatedAt(articleDto.getUpdatedAt());
			data.setDescription(articleDto.getDescription());
			data.setFavorited(false);
			data.setSlug(articleDto.toSlug(articleDto.getTitle()));

			// List<ArticleTagArticleDto> tagDtoList = articleDto.getTagList();
			List<ArticleTagArticleDto> tagDtoList = articleTagArticleRepo.findByArticleId(articleDto.getId());
			List<String> tagList = new ArrayList<String>();
			Optional<ArticleTagDto> tag = null;
			ArticleTagDto tagDto = null;
			for (ArticleTagArticleDto articleTag : tagDtoList) {

				tag = articleTagRepo.findById(articleTag.getTagId());

				if (tag.isPresent()) {
					tagDto = tag.get();
					tagList.add(tagDto.getName());
				}

			}

			data.setTagList(tagList);

			Integer userId = articleDto.getUserId();

			Optional<UserDto> userDto = userRepo.findById(userId);

			if (userDto.isPresent()) {
				UserDto user = userDto.get();

				profile = new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), true);

				data.setProfileData(profile);
			}
		}
		return data;
	}

	@Override
	public ArticleData findBySlug(String slug) {
		Optional<ArticleDto> article = articleRepo.findBySlug(slug);

		ArticleData data = null;
		ProfileData profile = null;
		ArticleDto articleDto = null;

		if (article.isPresent()) {
			articleDto = article.get();
			data = new ArticleData();

			data.setId(articleDto.getId());
			data.setBody(articleDto.getBody());
			data.setTitle(articleDto.getTitle());
			data.setCreatedAt(articleDto.getCreatedAt());
			data.setUpdatedAt(articleDto.getUpdatedAt());
			data.setDescription(articleDto.getDescription());
			data.setFavorited(false);
			data.setSlug(articleDto.toSlug(articleDto.getTitle()));

			// List<ArticleTagArticleDto> tagDtoList = articleDto.getTagList();
			List<ArticleTagArticleDto> tagDtoList = articleTagArticleRepo.findByArticleId(articleDto.getId());

			List<String> tagList = new ArrayList<String>();
			Optional<ArticleTagDto> tag = null;
			ArticleTagDto tagDto = null;
			for (ArticleTagArticleDto articleTag : tagDtoList) {

				tag = articleTagRepo.findById(articleTag.getTagId());

				if (tag.isPresent()) {
					tagDto = tag.get();
					tagList.add(tagDto.getName());
				}

			}

			data.setTagList(tagList);

			Integer userId = articleDto.getUserId();

			Optional<UserDto> userDto = userRepo.findById(userId);

			if (userDto.isPresent()) {
				UserDto user = userDto.get();

				profile = new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), true);

				data.setProfileData(profile);
			}
		}
		return data;
	}

	@Override
	public List<String> queryArticles(Pageable pageable, String tag, String author, String favoritedBy) {
		/*
		 * String search = "userId==" + 41; Node rootNode = new
		 * RSQLParser().parse(search); Specification<ArticleDto> spec =
		 * rootNode.accept(new CustomRsqlVisitor<ArticleDto>()); // return
		 * dao.findAll(spec); Page<ArticleDto> page = this.searchArticles(pageable,
		 * spec); // return new ResponseEntity<>(page, HttpStatus.OK); List<String> list
		 * = new ArrayList<String>();
		 * 
		 * for (ArticleDto element : page.getContent()) {
		 * list.add(element.getId().toString()); }
		 * 
		 */
		List<String> list = new ArrayList<String>();

		if (tag != null) {
			ArticleTagDto tagDto = articleTagRepo.findByName(tag);
			List<ArticleTagArticleDto> articles = null;

			if (tagDto != null) {
				articles = articleTagArticleRepo.findByTagId(tagDto.getId());
				for (ArticleTagArticleDto element : articles) {
					list.add(element.getArticleId().toString());
				}
				return list;
			}
		}

		List<ArticleDto> articleDtoList = null;
		UserDto userDto = null;
		if (author != null) {
			Optional<UserDto> userDtoTemp = userRepo.findByUserName(author);
			if (userDtoTemp.isPresent()) {
				userDto = userDtoTemp.get();
				
				articleDtoList = articleRepo.findByUserId(userDto.getId());
				
				for(ArticleDto article: articleDtoList) {
					
					list.add(article.getId().toString());
					
				}

				return list;
			}
		}
		
		List<ArticleFavoriteDto> favoriteList = null;
		if(favoritedBy!=null) {
			Optional<UserDto> userDtoTemp = userRepo.findByUserName(favoritedBy);
			if (userDtoTemp.isPresent()) {
				userDto = userDtoTemp.get();
				
				favoriteList = articleFavoriteRepo.findByUserId(userDto.getId());
				
				for(ArticleFavoriteDto favorite: favoriteList) {
					
					list.add(favorite.getArticleId().toString());
					
				}
				
				return list;
				
			}
			
		}

		articleDtoList = articleRepo.findAll();

		for (ArticleDto element : articleDtoList) {
			list.add(element.getId().toString());
		}

		return list;
	}

	private Page<ArticleDto> searchArticles(Pageable pageable, Specification<ArticleDto> spec) {
		return articleRepo.findAll(spec, pageable);
	}

	@Override
	public int countArticle(String tag, String author, String favoritedBy) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ArticleData> findArticles(Pageable pageable, List<String> articleIds) {
		List<ArticleData> list = new ArrayList<ArticleData>();

		ArticleData data = null;
		ProfileData profile = null;

		Optional<ArticleDto> articleDtoTemp = null;

		ArticleDto articleDto = null;

		// Page page = articleRepo.findAll(pageable);

		// List<ArticleDto> pageList = page.getContent();

		for (String articleIdString : articleIds) {

			articleDtoTemp = articleRepo.findById(Integer.parseInt(articleIdString));

			if (articleDtoTemp.isPresent()) {
				data = new ArticleData();

				articleDto = articleDtoTemp.get();
				data.setId(articleDto.getId());
				data.setBody(articleDto.getBody());
				data.setTitle(articleDto.getTitle());
				data.setCreatedAt(articleDto.getCreatedAt());
				data.setUpdatedAt(articleDto.getUpdatedAt());
				data.setDescription(articleDto.getDescription());
				data.setFavorited(false);
				data.setSlug(articleDto.getSlug());

				// List<ArticleTagArticleDto> tagDtoList = articleDto.getTagList();
				List<ArticleTagArticleDto> tagDtoList = articleTagArticleRepo.findByArticleId(articleDto.getId());

				List<String> tagList = new ArrayList<String>();
				Optional<ArticleTagDto> tag = null;
				ArticleTagDto tagDto = null;
				for (ArticleTagArticleDto articleTag : tagDtoList) {

					tag = articleTagRepo.findById(articleTag.getTagId());

					if (tag.isPresent()) {
						tagDto = tag.get();
						tagList.add(tagDto.getName());
					}

				}

				data.setTagList(tagList);

				Integer userId = articleDto.getUserId();

				Optional<UserDto> userDto = userRepo.findById(userId);

				if (userDto.isPresent()) {
					UserDto user = userDto.get();

					profile = new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), true);

					data.setProfileData(profile);
				}

				list.add(data);

			}
		}

		return list;
	}

	@Override
	public List<ArticleData> findArticlesOfAuthors(Pageable pageable, List<Integer> authors) {
		List<ArticleData> list = new ArrayList<ArticleData>();
		List<ArticleDto> articleDtoList = null;
		ArticleData data = null;
		ProfileData profile = null;
		// ArticleDto articleDto = null;
		for (Integer id : authors) {

			articleDtoList = articleRepo.findByUserId(id);

			for (ArticleDto articleDto : articleDtoList) {

				data = new ArticleData();

				data.setId(articleDto.getId());
				data.setBody(articleDto.getBody());
				data.setTitle(articleDto.getTitle());
				data.setCreatedAt(articleDto.getCreatedAt());
				data.setUpdatedAt(articleDto.getUpdatedAt());
				data.setDescription(articleDto.getDescription());
				data.setFavorited(false);
				data.setSlug(articleDto.getSlug());

				Integer userId = articleDto.getUserId();

				Optional<UserDto> userDto = userRepo.findById(userId);

				if (userDto.isPresent()) {
					UserDto user = userDto.get();

					profile = new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), true);

					data.setProfileData(profile);
				}

				list.add(data);

			}
		}

		return list;
	}

	@Override
	public int countFeedSize(List<Integer> authors) {
		// TODO Auto-generated method stub
		return 0;
	}

}
