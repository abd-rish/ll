package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserPinnedPostsId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "POST_ID")
	private Long postId;
	
	public UserPinnedPostsId() {}

	public UserPinnedPostsId(Long userId, Long postId) {
		super();
		this.userId = userId;
		this.postId = postId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}
	
}
