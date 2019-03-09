package com.llp.util;

import java.util.List;

public class Query {

	private javax.persistence.Query query;
	
	public Query(javax.persistence.Query query) {
		this.query = query;
	}
	
	public Query setParameter(String name, Object value) {
		query.setParameter(name, value);
		return this;
	}
	
	public Query setStart(Integer start) {
		query.setFirstResult(start);
		return this;
	}
	
	public Query setLength(Integer length) {
		query.setMaxResults(length);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList() {
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSingleResult() {
		List<T> data = query.getResultList();
		if(data == null || data.isEmpty())
			return null;
		return data.get(0);
	}
	
	public void executeUpdate() {
		query.executeUpdate();
	}
	
}
