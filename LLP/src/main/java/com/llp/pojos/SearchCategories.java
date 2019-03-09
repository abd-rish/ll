package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "SEARCH_CATEGORIES")
public class SearchCategories extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private Byte id;
	
	@Column(name = "NAME")
	private String name;
	
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("SEARCH_CATEGORIES");
		queryParams.addId("ID", id);
		queryParams.add("NAME", name);
		return queryParams;
	}
	
	public SearchCategories() {}
	
	public SearchCategories(Byte id, String name) {
		this.id = id;
		this.name = name;
	}

	public Byte getId() {
		return id;
	}

	public void setId(Byte id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
