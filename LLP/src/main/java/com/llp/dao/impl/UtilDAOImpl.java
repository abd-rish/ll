package com.llp.dao.impl;

import org.springframework.stereotype.Repository;

import com.llp.dao.UtilDAO;
import com.llp.pojos.EmailTemplates;

@Repository
public class UtilDAOImpl extends AbstractDAOImpl implements UtilDAO {

	@Override
	public String getSuccessMessage(String code) {
		return createQuery("select message from ResSuccessMsg where code = :code").setParameter("code", code).getSingleResult();
	}
	
	@Override
	public String getErrorMessage(String code) {
		return createQuery("select message from ResErrorMsg where code = :code").setParameter("code", code).getSingleResult();
	}
	
	@Override
	public EmailTemplates getEmailTemplate(String code) {
		return createQuery("from EmailTemplates where code = :code").setParameter("code", code).getSingleResult();
	}
	
}
