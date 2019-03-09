package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "USER_RELATED_INFO")
public class UserRelatedInfo  extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "INFO")
	private String info;

	@Override
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("USER_RELATED_INFO");
		queryParams.addId("USER_ID", userId);
		queryParams.add("INFO", info);
		return queryParams;
	}

	public UserRelatedInfo() {}
	
	public UserRelatedInfo(Long userId, String info) {
		super();
		this.userId = userId;
		this.info = info;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
}
