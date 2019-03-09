package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PostRateId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "POST_ID")
	private Long postId;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	public PostRateId() {}

	public PostRateId(Long postId, Long userId) {
		super();
		this.postId = postId;
		this.userId = userId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
