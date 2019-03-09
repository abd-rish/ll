package com.llp.dao;

import java.util.List;

import com.llp.pojos.Countries;
import com.llp.pojos.UserRelatedInfo;
import com.llp.pojos.Users;

public interface ApplicationDAO extends AbstractDAO {

	public List<Countries> getAllCountries();
	
	public boolean isExistingEmail(String email);
	
	public boolean isExistingUsername(String username);
	
	public boolean isNotExistingCountry(String code);
	
	public Users getUser(String username);
	
	public Users getUserByUsernameOrEmail(String usernameOrEmail);
	
	public UserRelatedInfo getUserRelatedInfo(Long userId);
	
}
