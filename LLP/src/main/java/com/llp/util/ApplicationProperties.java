package com.llp.util;

public interface ApplicationProperties {

	final String ORIGIN = "http://localhost:4200";
	
	public interface Mail {
		final String HOST = "smtp.gmail.com";
		final String PORT = "587";
		final String AUTH = "true";
		final String STARTTLS_ENABLE = "true";
		final String STARTTLS_REQUIRED = "true";
		final String FROM = "llpfortt@gmail.com";
		final String PASSWORD = "llpfortt123";
	}
	
}
