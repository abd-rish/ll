package com.llp.pojos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "POST_COMMENTS")
public class PostComments extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PostCommentsId id;
	
	@Column(name = "COMMENT")
	private String comment;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("POST_COMMENTS");
		queryParams.addId("COMMENT_ID", id.getCommentId());
		queryParams.addId("USER_ID", id.getUserId());
		queryParams.addId("POST_ID", id.getPostId());
		queryParams.add("COMMENT", comment);
		queryParams.add("CREATED_DATE", createdDate);
		queryParams.add("UPDATED_DATE", updatedDate);
		return queryParams;
	}
	
	public PostComments() {}

	public PostComments(PostCommentsId id, String comment, Date createdDate) {
		super();
		this.id = id;
		this.comment = comment;
		this.createdDate = createdDate;
	}

	public PostCommentsId getId() {
		return id;
	}

	public void setId(PostCommentsId id) {
		this.id = id;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
