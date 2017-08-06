package com.taotao.manage.service;

import org.springframework.stereotype.Service;

import com.taotao.common.spring.exetend.PropertyConfig;

@Service
	public class PropertieService {
		/**
		 * 读取配置文件
		 */
    @PropertyConfig
    public String REPOSITORY_PATH;
    
    @PropertyConfig
    public String IMAGE_BASE_URL;

}
