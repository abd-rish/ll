package com.llp.security;

import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.llp.dao.ApplicationDAO;
import com.llp.pojos.Users;
import com.llp.shared.classes.UserInfo;
import com.llp.util.Base64;
import com.llp.util.SystemConstants;

@Component
public class RequestInterceptor implements HandlerInterceptor {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private ApplicationDAO applicationDAO;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(request.getMethod() != null && request.getMethod().toUpperCase().equals("OPTIONS"))
			return true;
		if(request.getRequestURI() != null && request.getRequestURI().startsWith("/app/"))
			return true;
		String authorization = request.getHeader("Authorization");
		if(authorization == null || authorization.length() < 7) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		try {
			authorization = Base64.decode(authorization.substring(6));
		} catch(Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		StringTokenizer tokenizer = new StringTokenizer(authorization, ":");
		if(tokenizer.countTokens() != 2) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		String usernameOrEmail = tokenizer.nextToken();
		String password = tokenizer.nextToken();
		Users user = applicationDAO.getUserByUsernameOrEmail(usernameOrEmail);
		if(user == null || !user.getPassword().equals(password)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
//		if(user.getActiveFlag().equals(SystemConstants.UserActiveStatus.FIRST_SIGN_IN)) {
//			response.setContentType("application/json");
//			response.setCharacterEncoding("UTF-8");
//			response.setStatus(HttpServletResponse.SC_OK);
//			response.getWriter().write("{\"returnValue\":{\"firstSignIn\":true,\"userInfo\":" + new UserInfo(user).toString() + "}}");
//			return false;
//		}
		
		session.setAttribute(SystemConstants.SessionVariables.USER_INFO, user);
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
