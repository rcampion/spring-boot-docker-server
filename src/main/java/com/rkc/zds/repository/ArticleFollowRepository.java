package com.rkc.zds.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.dto.ArticleFollowDto;

public interface ArticleFollowRepository extends JpaRepository<ArticleFollowDto, Integer> {

	ArticleFollowDto findByUserIdAndFollowId(Integer userId, Integer followId);
	
	List<ArticleFollowDto> findByUserId(Integer userId);
	
	ArticleFollowDto save(ArticleFollowDto follow);
}
