package com.llp.util;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONHelper {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static Map<String, Object> toMap(String json) {
		try {
		    return objectMapper.readValue(json, Map.class);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String toJSON(Map map) {
		try {
		    return objectMapper.writeValueAsString(map);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
