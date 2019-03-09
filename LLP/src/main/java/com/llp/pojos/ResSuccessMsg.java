package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "RES_SUCCESS_MSG")
public class ResSuccessMsg extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "MESSAGE")
	private String message;
	
	@Column(name = "TYPE_ID")
	private Byte typeId;
	
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("RES_SUCCESS_MSG");
		queryParams.addId("CODE", code);
		queryParams.add("MESSAGE", message);
		queryParams.add("TYPE_ID", typeId);
		return queryParams;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Byte getTypeId() {
		return typeId;
	}

	public void setTypeId(Byte typeId) {
		this.typeId = typeId;
	}
	
}
