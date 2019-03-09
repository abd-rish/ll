package com.llp.pojos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "USERS")
public class Users extends AbstractPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "GENDER")
	private Character gender;

	@Column(name = "BIRTH_DATE")
	private Date birthDate;

	@Column(name = "COUNTRY_CODE")
	private String countryCode;

	@Column(name = "CITY")
	private String city;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "ACTIVE_FLAG")
	private Character activeFlag;
	
	@Column(name = "PROFILE_PICTURE_SRC")
	private String profilePictureSrc;
	
	@Column(name = "COVER_PHOTO_SRC")
	private String coverPhotoSrc;
	
	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("USERS");
		queryParams.addId("USER_ID", userId);
		queryParams.add("FIRST_NAME", firstName);
		queryParams.add("LAST_NAME", lastName);
		queryParams.add("GENDER", gender);
		queryParams.add("BIRTH_DATE", birthDate);
		queryParams.add("COUNTRY_CODE", countryCode);
		queryParams.add("CITY", city);
		queryParams.add("USERNAME", username);
		queryParams.add("EMAIL", email);
		queryParams.add("PASSWORD", password);
		queryParams.add("ACTIVE_FLAG", activeFlag);
		queryParams.add("PROFILE_PICTURE_SRC", profilePictureSrc);
		queryParams.add("COVER_PHOTO_SRC", coverPhotoSrc);
		return queryParams;
	}
	
	public Users() {}
	
	public Users(String firstName, String lastName, Character gender, Date birthDate, String countryCode,
			String city, String username, String email, String password, Character activeFlag) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.countryCode = countryCode;
		this.city = city;
		this.username = username;
		this.email = email;
		this.password = password;
		this.activeFlag = activeFlag;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public Character getActiveFlag() {
		return activeFlag;
	}
	
	public void setActiveFlag(Character activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	public String getProfilePictureSrc() {
		return profilePictureSrc;
	}
	
	public void setProfilePictureSrc(String profilePictureSrc) {
		this.profilePictureSrc = profilePictureSrc;
	}
	
	public String getCoverPhotoSrc() {
		return coverPhotoSrc;
	}
	
	public void setCoverPhotoSrc(String coverPhotoSrc) {
		this.coverPhotoSrc = coverPhotoSrc;
	}
	
}
