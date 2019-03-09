package com.llp.util;

public class QueryParam {

	private String param;
	private Object value;
	
	public QueryParam(String param, Object value) {
		super();
		this.param = param;
		this.value = value;
	}
	
	public String getParam() {
		return param;
	}
	
	public Object getValue() {
		return value;
	}
}
