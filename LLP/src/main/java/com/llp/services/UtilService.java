package com.llp.services;

import org.springframework.http.ResponseEntity;

import com.llp.util.Result;

public interface UtilService {
	
	public ResponseEntity<Result> getResponse(Result result);

}
