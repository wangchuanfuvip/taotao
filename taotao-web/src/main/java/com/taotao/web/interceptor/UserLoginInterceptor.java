package com.taotao.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.util.CookieUtils;
import com.taotao.web.controller.UserController;
import com.taotao.web.pojo.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadlocal.UserThreadLocal;

public class UserLoginInterceptor implements HandlerInterceptor {

	    @Autowired
	    private UserService userService;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	//从Cookie中获取ticket 
		String ticket=CookieUtils.getCookieValue(request, UserController.TT_TICKET);
		//未登录
		if (ticket==null) {
			response.sendRedirect("/user/login.html");
			 UserThreadLocal.set(null);
			return false;
		}
		//动过ticket拿到User 对象 
		User user =this.userService.queryUserByTicket(ticket);
		//登陆超时
		if (user==null) {
			response.sendRedirect("/user/login.html");
			 UserThreadLocal.set(null);
			return false;
		}
		//拦截器中将use让对象放入到ThreadLocal
		  UserThreadLocal.set(user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
