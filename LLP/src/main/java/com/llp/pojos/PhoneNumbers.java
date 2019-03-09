package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "PHONE_NUMBERS")
public class PhoneNumbers extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;
	
	@Column(name = "USER_ID")
	private Long userId;
	
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("PHONE_NUMBERS");
		queryParams.addId("PHONE_NUMBER", phoneNumber);
		queryParams.add("USER_ID", userId);
		return queryParams;
	}
	
	public PhoneNumbers() {}

	public PhoneNumbers(String phoneNumber, Long userId) {
		super();
		this.phoneNumber = phoneNumber;
		this.userId = userId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
