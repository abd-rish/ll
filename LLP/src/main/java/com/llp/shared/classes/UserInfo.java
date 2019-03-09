package com.llp.shared.classes;

import com.llp.pojos.Users;

public class UserInfo {

	private String fullName;
	
	private String profilePictureSRC;
	
	private String firstName;
	
	public UserInfo() {}
	
	public UserInfo(Users user) {
		super();
		this.fullName = user.getFirstName() + " " + user.getLastName();
		this.profilePictureSRC = user.getProfilePictureSrc();
		this.firstName = user.getFirstName();
	}
	
	@Override
	public String toString() {
		return "{\"fullName\":\"" + fullName + "\",\"profilePictureSRC\":\"" + profilePictureSRC + "\",\"firstName\":\"" + firstName + "\"}";
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProfilePictureSRC() {
		return profilePictureSRC;
	}

	public void setProfilePictureSRC(String profilePictureSRC) {
		this.profilePictureSRC = profilePictureSRC;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
}
