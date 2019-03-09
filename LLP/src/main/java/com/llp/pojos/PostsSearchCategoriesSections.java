package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "POSTS_SEARCH_CATEGORIES_SECTIONS")
public class PostsSearchCategoriesSections extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PostsSearchCategoriesSectionsId id;
	
	@Override
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("POSTS_SEARCH_CATEGORIES_SECTIONS");
		queryParams.addId("POST_ID", id.getPostId());
		queryParams.addId("SEARCH_CATEGORY_SECTION_ID", id.getSearchCategorySectionId());
		return queryParams;
	}
	
	public PostsSearchCategoriesSections() {}

	public PostsSearchCategoriesSections(PostsSearchCategoriesSectionsId id) {
		super();
		this.id = id;
	}

	public PostsSearchCategoriesSectionsId getId() {
		return id;
	}

	public void setId(PostsSearchCategoriesSectionsId id) {
		this.id = id;
	}

}
