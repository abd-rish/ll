package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PostsSearchCategoriesSectionsId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "POST_ID")
	private Long postId;
	
	@Column(name = "SEARCH_CATEGORY_SECTION_ID")
	private Short searchCategorySectionId;
	
	public PostsSearchCategoriesSectionsId() {}

	public PostsSearchCategoriesSectionsId(Long postId, Short searchCategorySectionId) {
		super();
		this.postId = postId;
		this.searchCategorySectionId = searchCategorySectionId;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Short getSearchCategorySectionId() {
		return searchCategorySectionId;
	}

	public void setSearchCategorySectionId(Short searchCategorySectionId) {
		this.searchCategorySectionId = searchCategorySectionId;
	}

}
