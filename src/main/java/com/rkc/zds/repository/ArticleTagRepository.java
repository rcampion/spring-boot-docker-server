package com.rkc.zds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rkc.zds.dto.ArticleDto;
import com.rkc.zds.dto.ArticleTagDto;

public interface ArticleTagRepository extends JpaRepository<ArticleTagDto, Integer>, JpaSpecificationExecutor<ArticleTagDto> {
	
	ArticleTagDto findByName(String tag);


}
