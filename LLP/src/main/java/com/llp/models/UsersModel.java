package com.llp.models;

import java.util.Date;
import java.util.List;

public class UsersModel extends AbstractModel {

	public void setFirstName(String firstName) {
		set("firstName", firstName);
	}
	
	public String getFirstName() {
		return getString("firstName");
	}
	
	public void setLastName(String lastName) {
		set("lastName", lastName);
	}
	
	public String getLastName() {
		return getString("lastName");
	}
	
	public void setGender(Character gender) {
		set("gender", gender);
	}
	
	public Character getGender() {
		return getCharacter("gender");
	}
	
	public void setBirthDate(Date birthDate) {
		set("birthDate", birthDate);
	}
	
	public Date getBirthDate() {
		return getDate("birthDate");
	}
	
	public void setCountryCode(String countryCode) {
		set("countryCode", countryCode);
	}
	
	public String getCountryCode() {
		return getString("countryCode");
	}
	
	public void setCity(String city) {
		set("city", city);
	}
	
	public String getCity() {
		return getString("city");
	}
	
	public void setPhoneNumber(String phoneNumber) {
		set("phoneNumber", phoneNumber);
	}
	
	public String getPhoneNumber() {
		return getString("phoneNumber");
	}
	
	public void setUsername(String username) {
		set("username", username);
	}
	
	public String getUsername() {
		return getString("username");
	}
	
	public void setEmail(String email) {
		set("email", email);
	}
	
	public String getEmail() {
		return getString("email");
	}
	
	public void setPassword(String password) {
		set("password", password);
	}
	
	public String getPassword() {
		return getString("password");
	}
	
	public void setUsernameOrEmail(String usernameOrEmail) {
		set("usernameOrEmail", usernameOrEmail);
	}
	
	public String getUsernameOrEmail() {
		return getString("usernameOrEmail");
	}
	
	public void setConfirmationCode(String confirmationCode) {
		set("confirmationCode", confirmationCode);
	}
	
	public String getConfirmationCode() {
		return getString("confirmationCode");
	}
	
	public List<Short> getSearchCategoriesSectionsIds() {
		return getShortList("searchCategoriesSectionsIds");
	}
	
}
