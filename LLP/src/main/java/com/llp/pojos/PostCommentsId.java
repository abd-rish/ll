package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PostCommentsId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "COMMENT_ID")
	private Long commentId;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "POST_ID")
	private Long postId;
	
	public PostCommentsId() {}

	public PostCommentsId(Long commentId, Long userId, Long postId) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.postId = postId;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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
