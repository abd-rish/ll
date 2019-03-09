package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "SEARCH_CATEGORIES_SECTIONS")
public class SearchCategoriesSections extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private Short id;
	
	@Column(name = "SEARCH_CATEGORY_ID")
	private Byte searchCategoryId;
	
	@Column(name = "NAME")
	private String name;
	
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("SEARCH_CATEGORIES_SECTIONS");
		queryParams.addId("ID", id);
		queryParams.add("SEARCH_CATEGORY_ID", searchCategoryId);
		queryParams.add("NAME", name);
		return queryParams;
	}
	
	public SearchCategoriesSections() {}

	public SearchCategoriesSections(Short id, Byte searchCategoryId, String name) {
		super();
		this.id = id;
		this.searchCategoryId = searchCategoryId;
		this.name = name;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Byte getSearchCategoryId() {
		return searchCategoryId;
	}

	public void setSearchCategoryId(Byte searchCategoryId) {
		this.searchCategoryId = searchCategoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
