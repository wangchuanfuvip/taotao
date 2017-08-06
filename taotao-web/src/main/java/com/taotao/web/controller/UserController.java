package com.taotao.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.util.CookieUtils;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.service.UserService;

@RequestMapping("/user")
@Controller
public class UserController {

	  public static final String TT_TICKET = "TT_TICKET";
	  @Autowired
	    private UserService userService;
	    @RequestMapping("register")
	    public String register() {
	        return "register";
	    }

	    @RequestMapping("login")
	    public String login() {
	        return "login";
	    }
	    
	    //完成注册的动能
	    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult doRegister(@RequestParam("username") String username,
	            @RequestParam("password") String password, @RequestParam("phone") String phone) {
	        boolean success = this.userService.doRegister(username, password, phone);
	        if (success) {
	            return TaotaoResult.ok();
	        }
	        return TaotaoResult.build(201, "注册失败!");
	    }
	    
	    
	    //完成登录
	    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult doLogin(@RequestParam("username") String username,
	            @RequestParam("password") String password, HttpServletRequest request,
	            HttpServletResponse response) {
	        String ticket = this.userService.doLogin(username, password);
	        if (null == ticket) {
	            // 登录失败
	            return TaotaoResult.build(201, "登录失败!");
	        }
	        // 登录成功,将ticket写入到cookie中
	        CookieUtils.setCookie(request, response, TT_TICKET, ticket, 60 * 60 * 24);
	        return TaotaoResult.ok();
	    }
}
