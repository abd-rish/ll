package com.llp.pojos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "POSTS")
public class Posts extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "CONTENT")
	private String content;
	
	@Column(name = "RATE")
	private Integer rate;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
		
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("POSTS");
		queryParams.addId("ID", id);
		queryParams.add("USER_ID", userId);
		queryParams.add("CONTENT", content);
		queryParams.add("RATE", rate);
		queryParams.add("CREATED_DATE", createdDate);
		queryParams.add("UPDATED_DATE", updatedDate);
		return queryParams;
	}
	
	public Posts() {}

	public Posts(Long id, Long userId, String content, Date createdDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.content = content;
		this.createdDate = createdDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getRate() {
		return rate;
	}
	
	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
