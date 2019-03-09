package com.llp.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.llp.dao.ApplicationDAO;
import com.llp.pojos.Countries;
import com.llp.pojos.UserRelatedInfo;
import com.llp.pojos.Users;

@Repository
public class ApplicationDAOImpl extends AbstractDAOImpl implements ApplicationDAO {

	@Override
	public List<Countries> getAllCountries() {
		return createQuery("from Countries").getResultList();
	}
	
	@Override
	public boolean isExistingEmail(String email) {
		Object count = createQuery("select count(*) from Users where lower(email) = :email")
				.setParameter("email", email.toLowerCase())
				.getSingleResult();
		if(count == null)
			return false;
		if(count instanceof Long)
			return (Long) count > 0L;
		return Long.parseLong(count.toString()) > 0L;
	}
	
	@Override
	public boolean isExistingUsername(String username) {
		Object count = createQuery("select count(*) from Users where lower(username) = :username")
				.setParameter("username", username.toLowerCase())
				.getSingleResult();
		if(count == null)
			return false;
		if(count instanceof Long)
			return (Long) count > 0L;
		return Long.parseLong(count.toString()) > 0L;
	}
	
	@Override
	public boolean isNotExistingCountry(String code) {
		Object count = createQuery("select count(*) from Countries where code = :code").setParameter("code", code).getSingleResult();
		if(count == null)
			return true;
		if(count instanceof Long)
			return (Long) count == 0L;
		return Long.parseLong(count.toString()) == 0L;
	}
	
	@Override
	public Users getUser(String username) {
		return createQuery("from Users where lower(username) = :username").setParameter("username", username.toLowerCase()).getSingleResult();
	}
	
	@Override
	public Users getUserByUsernameOrEmail(String usernameOrEmail) {
		return createQuery("from Users where lower(username) = :usernameOrEmail or lower(email) = :usernameOrEmail")
				.setParameter("usernameOrEmail", usernameOrEmail.toLowerCase())
				.getSingleResult();
	}
	
	@Override
	public UserRelatedInfo getUserRelatedInfo(Long userId) {
		return createQuery("from UserRelatedInfo where userId = :userId").setParameter("userId", userId).getSingleResult();
	}
	
}
