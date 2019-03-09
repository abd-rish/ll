package com.llp.services;

import com.llp.models.UsersModel;
import com.llp.util.Result;

public interface ApplicationService {

	public Result getCountriesInfo();
	
	public Result signUp(UsersModel usersModel);
	
	public Result sendConfirmationCodeByEmail(UsersModel usersModel);
	
	public Result resetPassword(UsersModel usersModel);
	
	public Result signIn(UsersModel usersModel);
	
}
