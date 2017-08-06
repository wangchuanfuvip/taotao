package com.taotao.sso.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	/**
	 * 静态代码块
	 * @param param
	 * @param type
	 * @return
	 */
    @Autowired
    private RedisService redisService;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String TICKET="TICKET";

    private static final Integer TICKET_TIME = 60 * 60 *10;

	private static final Map <Integer ,String>  PARAM_TYPE=new HashMap<Integer,String>();
	static{
		PARAM_TYPE.put(1, "username");
		PARAM_TYPE.put(2, "phone");
		PARAM_TYPE.put(3, "email");
	}
	public TaotaoResult checkUserName(String param, Integer type) {
		if(!PARAM_TYPE.containsKey(type)){
			 return TaotaoResult.build(201, "校验类型不合法，只能是1、2、3.");
		}
		/**
		 * 从数据库进行查询
		 */
		 Integer count = this.userMapper.checkUserName(param, PARAM_TYPE.get(type));
		 if(count==0){
			 return TaotaoResult.ok(false);
		 }
		 return TaotaoResult.ok(true);
	}
	public boolean register(User user) {

		/**
		 * 用户名是否重复
		 */
		Integer count =this.userMapper.checkUserName(user.getUsername(), PARAM_TYPE.get(1));
		 if (count > 0) {
	            // 已经注册，此处注册失败
	            return false;
	        }
		 try{
			 this.userMapper.save(user);
		 }catch(Exception e){
			 e.printStackTrace();
		 }
	
		return true;
	}
	public String login(String userName, String passwd) {
		/**
		 * 通过用户名字,密码进行查询,查到数据,登陆成功,将ticket 保存到redis中
		 * 没数据,说明失败了
		 */
		
		 User user = this.userMapper.login(userName);
		 
		  if (null == user || !StringUtils.equals(user.getPassword(), DigestUtils.md5Hex(passwd))) {
	            return null;
	        }
		  
		  //动态的生成ticket
		  String ticket = DigestUtils.md5Hex(System.currentTimeMillis() + userName + user.getId());
		  
		  try {
			this.redisService.set(TICKET+"_"+ticket, MAPPER.writeValueAsString(user),TICKET_TIME);
			  return ticket;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return null;
	}
	public String queryUserByTicket(String ticket) {
		 String key = TICKET + "_" + ticket;
	        String data = this.redisService.get(key);
	        if (data == null) {
	            return null;
	        }
	        // 刷新数据的生存时间
	        this.redisService.expire(key, TICKET_TIME);
	        return data;
	    }
	}



