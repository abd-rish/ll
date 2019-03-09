package com.llp.util;

import java.util.LinkedList;
import java.util.List;

public class QueryParams {

	private String tableName;
	private List<QueryParam> queryIdParamList;
	private List<QueryParam> queryParamList;
	
	public QueryParams(String tableName) {
		this.tableName = tableName;
		queryIdParamList = new LinkedList<QueryParam>();
		queryParamList = new LinkedList<QueryParam>();
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void addId(String param, Object value) {
		queryIdParamList.add(new QueryParam(param, value));
	}
	
	public void add(String param, Object value) {
		queryParamList.add(new QueryParam(param, value));
	}
	
	public List<QueryParam> getIdParams() {
		return queryIdParamList;
	}
	
	public List<QueryParam> getParams() {
		return queryParamList;
	}
	
}
