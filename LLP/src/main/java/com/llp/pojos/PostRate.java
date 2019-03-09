package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "POST_RATE")
public class PostRate extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private PostRateId id;
	
	@Column(name = "RATE")
	private Byte rate;
	
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("POST_RATE");
		queryParams.addId("POST_ID", id.getPostId());
		queryParams.addId("USER_ID", id.getUserId());
		queryParams.add("RATE", rate);
		return queryParams;
	}
	
	public PostRate() {}

	public PostRate(PostRateId id, Byte rate) {
		super();
		this.id = id;
		this.rate = rate;
	}

	public PostRateId getId() {
		return id;
	}

	public void setId(PostRateId id) {
		this.id = id;
	}

	public Byte getRate() {
		return rate;
	}

	public void setRate(Byte rate) {
		this.rate = rate;
	}

}
