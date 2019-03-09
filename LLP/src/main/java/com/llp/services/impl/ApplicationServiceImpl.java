package com.llp.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.llp.dao.ApplicationDAO;
import com.llp.dao.UserDAO;
import com.llp.models.UsersModel;
import com.llp.pojos.Countries;
import com.llp.pojos.PhoneNumbers;
import com.llp.pojos.SearchCategories;
import com.llp.pojos.SearchCategoriesSections;
import com.llp.pojos.UserRelatedInfo;
import com.llp.pojos.Users;
import com.llp.services.ApplicationService;
import com.llp.util.Base64;
import com.llp.util.CharInterval;
import com.llp.util.Helper;
import com.llp.util.JSONHelper;
import com.llp.util.MailSender;
import com.llp.util.Result;
import com.llp.util.SystemConstants;

@Service
public class ApplicationServiceImpl implements ApplicationService {
	
	@Autowired
	private ApplicationDAO applicationDAO;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public Result getCountriesInfo() {
		Result result = new Result();
		
		List<Countries> countriesIterable = applicationDAO.getAllCountries();
		
		List<Map<String, Object>> countriesInfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> countryInfoMap;
		for(Countries country : countriesIterable) {
			countryInfoMap = new HashMap<String, Object>();
			countryInfoMap.put("code", country.getCode());
			countryInfoMap.put("label", country.getName());
			countryInfoMap.put("callingCode", country.getCallingCode());
			countriesInfoList.add(countryInfoMap);
		}
		
		Collections.sort(countriesInfoList, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return o1.get("label").toString().compareTo(o2.get("label").toString());
			}
		});
		
		result.setReturnValue(countriesInfoList);
		return result;
	}
	
	@Override
	public Result signUp(UsersModel usersModel) {
		Result result = new Result();
		
		String firstName = usersModel.getFirstName();
		String lastName = usersModel.getLastName();
		Character gender = usersModel.getGender();
		Date birthDate = usersModel.getBirthDate();
		String countryCode = usersModel.getCountryCode();
		String city = usersModel.getCity();
		String phoneNumber = usersModel.getPhoneNumber();
		String username = usersModel.getUsername();
		String email = usersModel.getEmail();
		String password = usersModel.getPassword();
		result = Helper.checkRequiredParams(result, firstName, lastName, gender, birthDate, countryCode, city, phoneNumber, username, email, password);
		if(!result.isExecutionSuccessful())
			return result;
		if(!firstName.matches(SystemConstants.Regex.NAME) || !lastName.matches(SystemConstants.Regex.NAME) || (gender != 'M' && gender != 'F') ||
				!city.matches(SystemConstants.Regex.NAME) || !phoneNumber.matches(SystemConstants.Regex.PHONE_NUMBER) ||
		        !username.matches(SystemConstants.Regex.USERNAME) || !email.matches(SystemConstants.Regex.EMAIL) ||
		        !password.matches(SystemConstants.Regex.PASSWORD))
			return Helper.getResultWithErrorCode(result, "0002");
		
		if(applicationDAO.isExistingEmail(email))
			return Helper.getResultWithErrorCode(result, "1000");
		if(applicationDAO.isExistingUsername(username))
			return Helper.getResultWithErrorCode(result, "1002");
		
		Date now = new Date();
		if(birthDate.after(now))
			return Helper.getResultWithErrorCode(result, "1001");
		Calendar minBirthDateCal = Calendar.getInstance();
		minBirthDateCal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 120);
		Calendar birthDateCal = Calendar.getInstance();
		birthDateCal.setTime(birthDate);
		if(birthDateCal.get(Calendar.YEAR) - minBirthDateCal.get(Calendar.YEAR) < 0)
			return Helper.getResultWithErrorCode(result, "1001");
		
		if(applicationDAO.isNotExistingCountry(countryCode))
			return Helper.getResultWithErrorCode(result, "0001");
		
		applicationDAO.savePojo(new Users(firstName, lastName, gender, birthDate, countryCode, city, username, email, password, SystemConstants.UserActiveStatus.ACTIVE));

		Long userId = applicationDAO.getUser(username).getUserId();
		
		applicationDAO.savePojo(new PhoneNumbers(phoneNumber, userId));
		
		String activationCode = Helper.createRandomCode(6, new CharInterval('0', '9'));
		applicationDAO.savePojo(new UserRelatedInfo(userId, "{\"activationCode\":\"" + activationCode + "\",\"activationStartDate\":" + new Date().getTime() + "}"));
		usersModel.put("activationCode", activationCode);
		mailSender.sendEmail(email, SystemConstants.EmailTemplates.SIGN_UP, usersModel);
		
		List<SearchCategories> searchCategories = userDAO.getSearchCategories();
		List<SearchCategoriesSections> searchCategoriesSections = userDAO.getSearchCategoriesSections();
//		if(searchCategories == null || searchCategories.isEmpty() || searchCategoriesSections == null || searchCategoriesSections.isEmpty())
//			return Helper.getResultWithErrorCode(result, "0001");
		Map<Byte, List<Map<String, Object>>> searchCategoriesSectionsMap = new HashMap<Byte, List<Map<String, Object>>>();
		Map<String, Object> tmpMap;
		for(SearchCategoriesSections section : searchCategoriesSections) {
    		if(searchCategoriesSectionsMap.get(section.getSearchCategoryId()) == null)
	    		searchCategoriesSectionsMap.put(section.getSearchCategoryId(), new ArrayList<Map<String, Object>>());
		    tmpMap = new HashMap<String, Object>();
			tmpMap.put("id", section.getId());
    		tmpMap.put("name", section.getName());
	     	searchCategoriesSectionsMap.get(section.getSearchCategoryId()).add(tmpMap);
		}
        List<Map<String, Object>> searchCategoriesList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> searchCategorySectionsList;
		for(SearchCategories searchCategory : searchCategories) {
			searchCategorySectionsList = searchCategoriesSectionsMap.get(searchCategory.getId());
			if(searchCategorySectionsList == null)
				continue;
			tmpMap = new HashMap<String, Object>();
			tmpMap.put("name", searchCategory.getName());
			tmpMap.put("sections", searchCategorySectionsList);
			searchCategoriesList.add(tmpMap);
		}
		
		
		usersModel.clear();
		usersModel.put("accessToken", "Basic " + Base64.encode(username + ":" + password));
		usersModel.put("searchCategoriesList", searchCategoriesList);
		result.setReturnValue(usersModel);
		
		return Helper.getResultWithSuccessCode(result, "1000");
	}
	
	@Override
	public Result sendConfirmationCodeByEmail(UsersModel usersModel) {
		Result result = new Result();
		
		String usernameOrEmail = usersModel.getUsernameOrEmail();
		if(usernameOrEmail == null || usernameOrEmail.isEmpty())
			return Helper.getResultWithErrorCode(result, "0000");
		
		Users user = applicationDAO.getUserByUsernameOrEmail(usernameOrEmail);
		if(user == null)
			return Helper.getResultWithErrorCode(result, "1003");
		
		UserRelatedInfo userRelatedInfo = applicationDAO.getUserRelatedInfo(user.getUserId());
		String confirmationCode = Helper.createRandomCode(6, new CharInterval('0', '9'));
		if(userRelatedInfo == null) {
			applicationDAO.savePojo(new UserRelatedInfo(user.getUserId(), "{\"" + SystemConstants.ResetPassword.CONFIRMATION_CODE + "\":\"" + confirmationCode + "\",\"" + SystemConstants.ResetPassword.CONFIRMATION_START_DATE + "\":" + new Date().getTime() + "}"));
		} else if(userRelatedInfo.getInfo() == null || userRelatedInfo.getInfo().trim().isEmpty()) {
			userRelatedInfo.setInfo("{\"" + SystemConstants.ResetPassword.CONFIRMATION_CODE + "\":\"" + confirmationCode + "\",\"" + SystemConstants.ResetPassword.CONFIRMATION_START_DATE + "\":" + new Date().getTime() + "}");
			applicationDAO.savePojo(userRelatedInfo);
		} else {
			Map<String, Object> infoMap = JSONHelper.toMap(userRelatedInfo.getInfo());
			if(infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_BLOCK_DATE) != null) {
				Object confirmationBlockDate = infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_BLOCK_DATE);
				if(Helper.calculateDateDiff(confirmationBlockDate instanceof Long ? (Long) confirmationBlockDate : Long.parseLong(confirmationBlockDate.toString()), new Date().getTime(), Calendar.MINUTE) < 10)
					return Helper.getResultWithErrorCode(result, "1005");
				infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_BLOCK_DATE);
			}
			if(infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_TRY_NUMBER) != null) {
				Long confirmationStartDate = infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_START_DATE) == null ? null : infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_START_DATE) instanceof Long ?
						(Long) infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_START_DATE) : Long.parseLong(infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_START_DATE).toString());
				Long now = new Date().getTime();
				if(confirmationStartDate == null
						|| Helper.calculateDateDiff(confirmationStartDate, now, Calendar.DAY_OF_YEAR) != 0
						|| Helper.calculateDateDiff(confirmationStartDate, now, Calendar.YEAR) != 0)
					infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_TRY_NUMBER);
			}
			infoMap.put(SystemConstants.ResetPassword.CONFIRMATION_CODE, confirmationCode);
			infoMap.put(SystemConstants.ResetPassword.CONFIRMATION_START_DATE, new Date().getTime());
			userRelatedInfo.setInfo(JSONHelper.toJSON(infoMap));
			applicationDAO.updatePojo(userRelatedInfo);
		}
		
		usersModel.setFirstName(user.getFirstName());
		usersModel.put("confirmationCode", confirmationCode);
		mailSender.sendEmail(user.getEmail(), SystemConstants.EmailTemplates.RESET_PASSWORD, usersModel);
		
		return Helper.getResultWithSuccessCode(result, "1001");
	}
	
	@Override
	public Result resetPassword(UsersModel usersModel) {
		Result result = new Result();
		
		String usernameOrEmail = usersModel.getUsernameOrEmail();
		String requestedConfirmationCode = usersModel.getConfirmationCode();
		String password = usersModel.getPassword();
		result = Helper.checkRequiredParams(result, usernameOrEmail, requestedConfirmationCode, password);
		if(!result.isExecutionSuccessful())
			return result;
		if(!password.matches(SystemConstants.Regex.PASSWORD))
			return Helper.getResultWithErrorCode(result, "0002");
		
		Users user = applicationDAO.getUserByUsernameOrEmail(usernameOrEmail);
		if(user == null)
			return Helper.getResultWithErrorCode(result, "1003");
		UserRelatedInfo userRelatedInfo = applicationDAO.getUserRelatedInfo(user.getUserId());
		if(userRelatedInfo == null || userRelatedInfo.getInfo() == null || userRelatedInfo.getInfo().trim().isEmpty())
			return Helper.getResultWithErrorCode(result, "1004");
		Map<String, Object> infoMap = JSONHelper.toMap(userRelatedInfo.getInfo());
		Long now = new Date().getTime();
		if(infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_BLOCK_DATE) != null) {
			Object confirmationBlockDateObject = infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_BLOCK_DATE);
			if(Helper.calculateDateDiff(confirmationBlockDateObject instanceof Long ? (Long) confirmationBlockDateObject : Long.parseLong(confirmationBlockDateObject.toString()), now, Calendar.MINUTE) < 10)
				return Helper.getResultWithErrorCode(result, "1005");
			infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_BLOCK_DATE);
		}
		if(infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_START_DATE) != null 
				&& Helper.calculateDateDiff(infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_START_DATE) instanceof Long 
						? (Long) infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_START_DATE) 
								: Long.parseLong(infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_START_DATE).toString()),
								now, Calendar.MINUTE) > 9) {
			infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_CODE);
			infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_START_DATE);
			if(infoMap.isEmpty())
				applicationDAO.deletePojo(userRelatedInfo);
			else {
				userRelatedInfo.setInfo(JSONHelper.toJSON(infoMap));
				applicationDAO.updatePojo(userRelatedInfo);
			}
			return Helper.getResultWithErrorCode(result, "1007");
		}
		if(infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_CODE) == null)
			return Helper.getResultWithErrorCode(result, "1004");
		String confirmationCode = infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_CODE).toString();
		if(confirmationCode.isEmpty())
			return Helper.getResultWithErrorCode(result, "1004");
		
		if(!requestedConfirmationCode.equals(confirmationCode)) {
			Object confirmationTryNumberObject = infoMap.get(SystemConstants.ResetPassword.CONFIRMATION_TRY_NUMBER);
			if(confirmationTryNumberObject == null) {
				infoMap.put(SystemConstants.ResetPassword.CONFIRMATION_TRY_NUMBER, 1);
				userRelatedInfo.setInfo(JSONHelper.toJSON(infoMap));
				applicationDAO.updatePojo(userRelatedInfo);
				return Helper.getResultWithErrorCode(result, "1006");
			}
			Byte confirmationTryNumber = confirmationTryNumberObject instanceof Byte ? (Byte) confirmationTryNumberObject : Byte.parseByte(confirmationTryNumberObject.toString());
			if(confirmationTryNumber < 2) {
				infoMap.put(SystemConstants.ResetPassword.CONFIRMATION_TRY_NUMBER, 2);
				userRelatedInfo.setInfo(JSONHelper.toJSON(infoMap));
				applicationDAO.updatePojo(userRelatedInfo);
				return Helper.getResultWithErrorCode(result, "1006");
			}
			infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_CODE);
			infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_START_DATE);
			infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_TRY_NUMBER);
			infoMap.put(SystemConstants.ResetPassword.CONFIRMATION_BLOCK_DATE, new Date().getTime());
			userRelatedInfo.setInfo(JSONHelper.toJSON(infoMap));
			applicationDAO.updatePojo(userRelatedInfo);
			return Helper.getResultWithErrorCode(result, "1005");
		}
		
		infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_CODE);
		infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_START_DATE);
		infoMap.remove(SystemConstants.ResetPassword.CONFIRMATION_TRY_NUMBER);
		if(infoMap.isEmpty())
			applicationDAO.deletePojo(userRelatedInfo);
		else {
			userRelatedInfo.setInfo(JSONHelper.toJSON(infoMap));
			applicationDAO.updatePojo(userRelatedInfo);
		}
		
		user.setPassword(password);
		applicationDAO.updatePojo(user);
		
		return Helper.getResultWithSuccessCode(result, "1002");
	}
	
	@Override
	public Result signIn(UsersModel usersModel) {
		Result result = new Result();
		
		String usernameOrEmail = usersModel.getUsernameOrEmail();
		String password = usersModel.getPassword();
		result = Helper.checkRequiredParams(result, usernameOrEmail, password);
		if(!result.isExecutionSuccessful())
			return result;
		
		Users user = applicationDAO.getUserByUsernameOrEmail(usernameOrEmail);
		if(user == null)
			return Helper.getResultWithErrorCode(result, "1003");
		
		if(user.getActiveFlag() == null || user.getActiveFlag().equals(SystemConstants.UserActiveStatus.BLOCKED))
			return Helper.getResultWithErrorCode(result, "1009");
		
		if(!user.getPassword().equals(password))
			return Helper.getResultWithErrorCode(result, "1008");
		
		usersModel.clear();
		usersModel.put("accessToken", "Basic " + Base64.encode(user.getUsername() + ":" + password));
		result.setReturnValue(usersModel);
		
		return result;
	}
	
}
