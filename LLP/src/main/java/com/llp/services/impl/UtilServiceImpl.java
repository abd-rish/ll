package com.llp.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.llp.dao.UtilDAO;
import com.llp.services.UtilService;
import com.llp.util.Result;

@Service
public class UtilServiceImpl implements UtilService {
	
	@Autowired
	private UtilDAO utilDAO;
	
	@Override
	public ResponseEntity<Result> getResponse(Result result) {
		if(result.isExecutionSuccessful()) {
			if(result.getSuccessCode() != null)
				result.setMessage(utilDAO.getSuccessMessage(result.getSuccessCode()));
		} else {
			if(result.getErrorCode() != null)
				result.setMessage(utilDAO.getErrorMessage(result.getErrorCode()));
		}
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}
	
}
