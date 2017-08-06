package com.taotao.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.spring.exetend.PropertyConfig;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.pojo.Item;
@Service
public class SearchService {
    @Autowired
    private ApiService apiService;

    @PropertyConfig
    private String SEARCH_TAOTAO;

    @PropertyConfig
    public Integer SEARCH_TAOTAO_COUNT;

	public TaotaoResult search(String keyWords, Integer page) {
		//封装集合
	Map <String, String> params =new HashMap<String, String>();
	params.put("keyWords", keyWords);
    params.put("page", String.valueOf(page));
    params.put("rows", String.valueOf(SEARCH_TAOTAO_COUNT));
		try {
			return TaotaoResult.formatToList(this.apiService.doPost(SEARCH_TAOTAO, params), Item.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
