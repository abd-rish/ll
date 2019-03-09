package com.llp.util;

import org.springframework.util.Base64Utils;

public class Base64 {

	public static String encode(String str) {
		if(str == null)
			return null;
		return Base64Utils.encodeToString(str.getBytes());
	}
	
	public static String decode(String str) {
		if(str == null)
			return null;
		return new String(Base64Utils.decodeFromString(str));
	}
	
}
