package com.llp.dao;

import com.llp.pojos.EmailTemplates;

public interface UtilDAO extends AbstractDAO {

	public String getSuccessMessage(String code);
	
	public String getErrorMessage(String code);
	
	public EmailTemplates getEmailTemplate(String code);
	
}
