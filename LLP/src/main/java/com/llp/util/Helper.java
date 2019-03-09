package com.llp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Helper {
	
	public static Result getResultWithSuccessCode(Result result, String successCode) {
		result.setErrorCode(null);
		result.setExecutionSuccessful(true);
		result.setSuccessCode(successCode);
		return result;
	}
	
	public static Result getResultWithErrorCode(Result result, String errorCode) {
		result.clean();
		result.setExecutionSuccessful(false);
		result.setErrorCode(errorCode);
		return result;
	}

	public static Result checkRequiredParams(Result result, Object ... params) {
		for(Object param : params)
			if(param == null || param.toString().isEmpty())
				return getResultWithErrorCode(result, "0000");
		return result;
	}
	
	public static String replaceVariables(String str, Map<String, Object> values) {
		if(str == null)
			return null;
		if(values == null || str.length() < 3)
			return str;
		StringBuilder stringBuilder = new StringBuilder();
		int l = str.length() - 1, i = 0;
		for(int j; i < l; i++) {
			if(str.charAt(i) != '$' || str.charAt(i + 1) != '{') {
				stringBuilder.append(str.charAt(i));
				continue;
			}
			j = i + 2;
			for(i = j; i <= l && str.charAt(i) != '}'; i++);
			if(i > l) {
				stringBuilder.append(str.substring(j - 2));
				break;
			}
			stringBuilder.append(values.get(str.substring(j, i)));
		}
		if(i == l)
			stringBuilder.append(str.charAt(l));
		return stringBuilder.toString();
	}
	
	public static String createRandomCode(int length, CharInterval ... charIntervals) {
		List<Character> charList = new LinkedList<Character>();
		if(charIntervals != null)
			for(CharInterval charInterval : charIntervals)
				if(charInterval != null)
		            for(char ch = charInterval.getFrom(); ch <= charInterval.getTo(); ch++)
			            charList.add(ch);
		char[] randomCharArray = new char[charList.size()];
		for(int i = 0; i < randomCharArray.length; i++)
			randomCharArray[i] = charList.remove((int) (Math.random() * (double) charList.size()));
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < length; i++)
			stringBuilder.append(randomCharArray[(int) (Math.random() * (double) randomCharArray.length)]);
		return stringBuilder.toString();
	}
	
	public static long calculateDateDiff(long from, long to, int calendarField) {
		Calendar fromCal = Calendar.getInstance();
		fromCal.setTimeInMillis(from);
		Calendar toCal = Calendar.getInstance();
		toCal.setTimeInMillis(to);
		return toCal.get(calendarField) - fromCal.get(calendarField);
	}
	
	public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
}
