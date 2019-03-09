package com.llp.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.llp.util.QueryParams;

@Entity
@Table(name = "EMAIL_TEMPLATES")
public class EmailTemplates extends AbstractPojo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "SUBJECT")
	private String subject;
	
	@Column(name = "TEMPLATE")
	private String template;

	public QueryParams getQueryParams() {
		QueryParams queryParams = new QueryParams("EMAIL_TEMPLATES");
		queryParams.addId("CODE", code);
		queryParams.add("SUBJECT", subject);
		queryParams.add("TEMPLATE", template);
		return queryParams;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
}
