package com.llp.models;

import java.util.List;

public class PostsModel extends AbstractModel {
	
	public void setPostId(Long postId) {
		set("postId", postId);
	}
	
	public Long getPostId() {
		return getLong("postId");
	}
	
	public void setComment(String comment) {
		set("comment", comment);
	}
	
	public String getComment() {
		return getString("comment");
	}
	
	public void setCommentId(Long commentId) {
		set("commentId", commentId);
	}
	
	public Long getCommentId() {
		return getLong("commentId");
	}
	
	public void setRate(Byte rate) {
		set("rate", rate);
	}
	
	public Byte getRate() {
		return getByte("rate");
	}
	
	public void setContent(String content) {
		set("content", content);
	}
	
	public String getContent() {
		return getString("content");
	}
	
	public List<Short> getSearchCategoriesSectionsIds() {
		return getShortList("searchCategoriesSectionsIds");
	}

}
