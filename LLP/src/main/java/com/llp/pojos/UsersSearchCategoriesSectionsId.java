package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UsersSearchCategoriesSectionsId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "SEARCH_CATEGORY_SECTION_ID")
	private Short searchCategorySectionId;
	
	public UsersSearchCategoriesSectionsId() {}

	public UsersSearchCategoriesSectionsId(Long userId, Short searchCategorySectionId) {
		super();
		this.userId = userId;
		this.searchCategorySectionId = searchCategorySectionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Short getSearchCategorySectionId() {
		return searchCategorySectionId;
	}

	public void setSearchCategorySectionId(Short searchCategorySectionId) {
		this.searchCategorySectionId = searchCategorySectionId;
	}

}
