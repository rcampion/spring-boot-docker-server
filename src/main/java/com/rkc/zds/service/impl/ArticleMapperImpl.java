package com.rkc.zds.service.impl;
import org.springframework.stereotype.Service;
import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.core.article.Tag;
import com.rkc.zds.service.ArticleMapper;

@Service
public class ArticleMapperImpl implements ArticleMapper {

	@Override
	public void insert(ArticleDto article) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArticleDto findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean findTag(String tagName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insertTag(Tag tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertArticleTagRelation(String articleId, String tagId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArticleDto findBySlug(String slug) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(ArticleDto article) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

}
