package com.rkc.zds.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.UUID;
import java.util.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PCM_ARTICLE_COMMENTS")
public class ArticleCommentDto {
	@Id
	@Column(name="ID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private Integer id;
	@Column(name="USER_ID")	
    private Integer userId;
	@Column(name="ARTICLE_ID")	
    private Integer articleId;
	@Column(name="BODY")	
    private String body;
	@Column(name="CREATED_AT")	
    private Timestamp createdAt;
/*
    public ArticleCommentDto(String body, Integer userId, Integer articleId) {
        this.id = Integer.parseInt(UUID.randomUUID().toString());
        this.body = body;
        this.userId = userId;
        this.articleId = articleId;
        this.createdAt = new Date();
    }
*/    
    public ArticleCommentDto() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}


}
