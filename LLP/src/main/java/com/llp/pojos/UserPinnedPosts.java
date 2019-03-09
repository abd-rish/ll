package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "USER_PINNED_POSTS")
public class UserPinnedPosts extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private UserPinnedPostsId id;
	
	@Override
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("USER_PINNED_POSTS");
		queryParams.addId("USER_ID", id.getUserId());
		queryParams.addId("POST_ID", id.getPostId());
		return queryParams;
	}
	
	public UserPinnedPosts() {}

	public UserPinnedPosts(UserPinnedPostsId id) {
		super();
		this.id = id;
	}

	public UserPinnedPostsId getId() {
		return id;
	}

	public void setId(UserPinnedPostsId id) {
		this.id = id;
	}

}
