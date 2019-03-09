package com.llp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.llp.dao.AbstractDAO;
import com.llp.pojos.AbstractPojo;
import com.llp.util.Query;
import com.llp.util.QueryParam;
import com.llp.util.QueryParams;

@Repository
public class AbstractDAOImpl implements AbstractDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	protected Query createQuery(String qlString) {
		return new Query(entityManager.createQuery(qlString));
	}
	
	protected Query createSQLQuery(String sqlString) {
		return new Query(entityManager.createNativeQuery(sqlString));
	}
	
	@Override
	@Transactional
	public <P extends AbstractPojo> void savePojo(P p) {
		QueryParams queryParams = p.getQueryParams();
		List<QueryParam> idParams = queryParams.getIdParams();
		if(queryParams.getTableName() == null || idParams == null || idParams.isEmpty())
			return;
		List<QueryParam> params = queryParams.getParams();
		if(params == null)
			return;
		StringBuilder queryString = new StringBuilder();
		queryString.append("INSERT INTO ").append(queryParams.getTableName()).append('(');
		for(QueryParam idParam : idParams) {
			if(idParam == null || idParam.getParam() == null || idParam.getParam().isEmpty())
				return;
			queryString.append(idParam.getParam()).append(',');
		}
		for(QueryParam param : params) {
			if(param == null || param.getParam() == null || param.getParam().isEmpty())
				return;
			queryString.append(param.getParam()).append(',');
		}
		queryString.setCharAt(queryString.length() - 1, ')');
		queryString.append("VALUES(");
		for(QueryParam idParam : idParams)
			queryString.append(':').append(idParam.getParam()).append(',');
		for(QueryParam param : params)
			queryString.append(':').append(param.getParam()).append(',');
		queryString.setCharAt(queryString.length() - 1, ')');
		javax.persistence.Query query = entityManager.createNativeQuery(queryString.toString());
		for(QueryParam idParam : idParams)
			query.setParameter(idParam.getParam(), idParam.getValue());
		for(QueryParam param : params)
			query.setParameter(param.getParam(), param.getValue());
		query.executeUpdate();
	}
	
	@Override
	@Transactional
	public <P extends AbstractPojo> void updatePojo(P p) {
		QueryParams queryParams = p.getQueryParams();
		List<QueryParam> idParams = queryParams.getIdParams();
		if(queryParams.getTableName() == null || idParams == null || idParams.isEmpty())
			return;
		List<QueryParam> params = queryParams.getParams();
		if(params == null || params.isEmpty())
			return;
		StringBuilder queryString = new StringBuilder();
		queryString.append("UPDATE ").append(queryParams.getTableName()).append(" SET ");
		for(QueryParam param : params)
			queryString.append(param.getParam()).append("=:").append(param.getParam()).append(',');
		queryString.setCharAt(queryString.length() - 1, ' ');
		queryString.append("WHERE");
		boolean first = true;
		for(QueryParam idParam : idParams) {
			if(first) {
				queryString.append(" ").append(idParam.getParam()).append("=:").append(idParam.getParam());
				first = false;
				continue;
			}
			queryString.append(" AND ").append(idParam.getParam()).append("=:").append(idParam.getParam());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(queryString.toString());
		for(QueryParam param : params)
			query.setParameter(param.getParam(), param.getValue());
		for(QueryParam idParam : idParams)
			query.setParameter(idParam.getParam(), idParam.getValue());
		query.executeUpdate();
	}
	
	@Override
	@Transactional
	public <P extends AbstractPojo> void deletePojo(P p) {
		QueryParams queryParams = p.getQueryParams();
		List<QueryParam> idParams = queryParams.getIdParams();
		if(queryParams.getTableName() == null || idParams == null || idParams.isEmpty())
			return;
		StringBuilder queryString = new StringBuilder();
		queryString.append("DELETE FROM ").append(queryParams.getTableName()).append(" WHERE ");
		boolean first = true;
		for(QueryParam idParam : idParams) {
			if(first) {
				queryString.append(idParam.getParam()).append("=:").append(idParam.getParam());
				first = false;
				continue;
			}
			queryString.append(" AND ").append(idParam.getParam()).append("=:").append(idParam.getParam());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(queryString.toString());
		for(QueryParam idParam : idParams)
			query.setParameter(idParam.getParam(), idParam.getValue());
		query.executeUpdate();
	}
	
	@Override
	@Transactional
	public Long count(String table, QueryParam ... params) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT COUNT(*) FROM ").append(table);
		if(params != null) {
			queryString.append(" WHERE ");
			boolean first = true;
    		for(QueryParam param : params) {
	    		if(first) {
		    		queryString.append(param.getParam()).append("=:").append(param.getParam());
			    	first = false;
				    continue;
	    		}
		    	queryString.append(" AND ").append(param.getParam()).append("=:").append(param.getParam());
	    	}
		}
		Query query = createSQLQuery(queryString.toString());
		if(params != null)
		    for(QueryParam param : params)
			    query.setParameter(param.getParam(), param.getValue());
		Object count = query.getSingleResult();
		if(count == null)
			return 0L;
		return count instanceof Long ? (Long) count : Long.parseLong(count.toString());
	}
	
	@Override
	@Transactional
	public boolean isExists(String table, QueryParam ... params) {
		return count(table, params) > 0L;
	}

	@Override
	@Transactional
	public void delete(String table, QueryParam ... params) {
		StringBuilder queryString = new StringBuilder();
		queryString.append("DELETE FROM ").append(table);
		if(params != null && params.length > 0) {
			queryString.append(" WHERE ");
			boolean first = true;
    		for(QueryParam param : params) {
	    		if(first) {
		    		queryString.append(param.getParam()).append("=:").append(param.getParam());
			    	first = false;
				    continue;
	    		}
		    	queryString.append(" AND ").append(param.getParam()).append("=:").append(param.getParam());
	    	}
	    	javax.persistence.Query query = entityManager.createNativeQuery(queryString.toString());
			for(QueryParam param : params)
				query.setParameter(param.getParam(), param.getValue());
			query.executeUpdate();
		} else
			entityManager.createNativeQuery(queryString.toString()).executeUpdate();
	}
	
}
