package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "USERS_SEARCH_CATEGORIES_SECTIONS")
public class UsersSearchCategoriesSections extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private UsersSearchCategoriesSectionsId id;
	
	@Override
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("USERS_SEARCH_CATEGORIES_SECTIONS");
		queryParams.addId("USER_ID", id.getUserId());
		queryParams.addId("SEARCH_CATEGORY_SECTION_ID", id.getSearchCategorySectionId());
		return queryParams;
	}
	
	public UsersSearchCategoriesSections() {}

	public UsersSearchCategoriesSections(UsersSearchCategoriesSectionsId id) {
		super();
		this.id = id;
	}

	public UsersSearchCategoriesSectionsId getId() {
		return id;
	}

	public void setId(UsersSearchCategoriesSectionsId id) {
		this.id = id;
	}

}
