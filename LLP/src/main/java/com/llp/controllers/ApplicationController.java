package com.llp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.llp.models.UsersModel;
import com.llp.services.ApplicationService;
import com.llp.services.UtilService;
import com.llp.util.ApplicationProperties;
import com.llp.util.Result;

@RestController
@CrossOrigin(origins = ApplicationProperties.ORIGIN)
@RequestMapping("/app")
public class ApplicationController {

	@Autowired
	private UtilService utilService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@RequestMapping(value = "/getCountriesInfo", method = RequestMethod.GET)
	public ResponseEntity<Result> getCountriesInfo() {
		return utilService.getResponse(applicationService.getCountriesInfo());
	}
	
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public ResponseEntity<Result> signUp(@RequestBody UsersModel usersModel) {
		return utilService.getResponse(applicationService.signUp(usersModel));
	}
	
	@RequestMapping(value = "/sendConfirmationCodeByEmail", method = RequestMethod.POST)
	public ResponseEntity<Result> sendConfirmationCodeByEmail(@RequestBody UsersModel usersModel) {
		return utilService.getResponse(applicationService.sendConfirmationCodeByEmail(usersModel));
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<Result> resetPassword(@RequestBody UsersModel usersModel) {
		return utilService.getResponse(applicationService.resetPassword(usersModel));
	}
	
	@RequestMapping(value = "/signIn", method = RequestMethod.POST)
	public ResponseEntity<Result> signIn(@RequestBody UsersModel usersModel) {
		return utilService.getResponse(applicationService.signIn(usersModel));
	}
	
}
