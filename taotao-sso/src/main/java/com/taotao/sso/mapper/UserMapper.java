package com.taotao.sso.mapper;

import org.apache.ibatis.annotations.Param;

import com.taotao.sso.pojo.User;


public interface UserMapper {

	/**
	 * 检查数据的合法性,两个参数要加上@Param
	 * @param param
	 * @param object
	 * @return
	 */

	Integer checkUserName( @Param ("param")String param, @Param ("paramType") String paramType);
	/**
	 * 注册
	 * @param user
	 */
	void save(User user);
	User login(String userName);

}
