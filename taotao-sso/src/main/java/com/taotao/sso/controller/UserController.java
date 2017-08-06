package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import com.taotao.sso.utils.MD5;

@Controller
@RequestMapping("/user")
public class UserController {
	
	/**
	 * 校验数据的合法性
	 */
	@Autowired
	private UserService  userService;
	@RequestMapping(value = "/check/{param}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult check(@PathVariable("param") String param,
			@PathVariable("type") Integer type) {
	
		return this.userService.checkUserName(param, type);

	}

	
	/**
	 * 用户注册
	 * @Valid 进行数据校验
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(@Valid User user, BindingResult result) {
		
		/**
		 * 对数据的校验
		 */
		if(result.hasErrors()){
			/**
			 * 获取所有错误信息
			 * 
			 */
			List<ObjectError> errors=	result.getAllErrors();
			List <String>list=new ArrayList<String>();
			for (ObjectError error : errors) {
			   list.add(error.getDefaultMessage())	;
			}
			return TaotaoResult.build(201, StringUtils.join(errors, '|'));

		}
		/**
		 * 密码加密
		 */
		
		boolean boo = this.userService.register(user);
		if(boo){
			return TaotaoResult.ok();

		}
		return TaotaoResult.build(201, "注册失败!用户名重复");

	}
	/**
	 * 登录接口
	 */
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	
	public TaotaoResult login(@RequestParam ("u")String userName,@RequestParam("p") String passwd){
		String ticket = this.userService.login(userName, passwd);
		//判断ticket
		if(ticket==null){
			return TaotaoResult.build(201, "用户名或密码错误,登录失败");
		}
		return TaotaoResult.ok(ticket);
	}
	/**
	 * 通过ticket 查询用户信息
	 * 
	 */
	@RequestMapping(value = "query/{ticket}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult login(@PathVariable("ticket") String ticket) {
		// 返回
		String userJson = null;
		try {
			userJson = this.userService.queryUserByTicket(ticket);
			if (userJson == null) {
				return TaotaoResult.build(201, "查询失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(201, "查询失败！");
		}
		return TaotaoResult.ok(userJson);
	}

	
}
