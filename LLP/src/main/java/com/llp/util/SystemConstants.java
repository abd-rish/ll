package com.llp.util;

public interface SystemConstants {

	public interface Regex {
		String NAME = "^[A-Za-z .]{1,30}$";
		String PHONE_NUMBER = "^[0-9]{1,10}$";
		String EMAIL = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		String USERNAME = "^[0-9A-Za-z._ -]{1,30}$";
		String PASSWORD = "^.{8,30}$";
	}
	
	public interface EmailTemplates {
		String SIGN_UP = "SIGN_UP";
		String RESET_PASSWORD = "RESET_PASSWORD";
	}
	
	public interface ResetPassword {
		String CONFIRMATION_CODE = "confirmationCode";
		String CONFIRMATION_START_DATE = "confirmationStartDate";
		String CONFIRMATION_BLOCK_DATE = "confirmationBlockDate";
		String CONFIRMATION_TRY_NUMBER = "confirmationTryNumber";
	}
	
	public interface UserActiveStatus {
		Character ACTIVE = 'A';
		Character BLOCKED = 'B';
	}
	
	public interface SessionVariables {
		String USER_INFO = "userInfo";
	}
	
	public interface MySQL {
		
		public interface DataTypes {
			int TEXT_MAX_LENGTH = 60000;
		}
		
	}
	
	public interface Post {
		int RATE_DIGITS = 10000000;
	}
	
}
