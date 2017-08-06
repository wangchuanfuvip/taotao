package com.taotao.search.test;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.common.vo.EasyUIResult;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ApiService;

public class SaveDataToSolr {
	
	   private HttpSolrServer httpSolrServer;

	    private ApiService apiService;
	    @Before
	    public void before() {
	        // 创建solrServer对象
	        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
	                "spring/applicationContext*.xml");
	        this.httpSolrServer = applicationContext.getBean(HttpSolrServer.class);
	        this.apiService = applicationContext.getBean(ApiService.class);
	    }

	    // 查询商品数据
	    @Test
	    public void testData() throws Exception {
	        // 查询商品数据
	        String url = "http://manage.taotao.com/item/query?page={page}&rows=100";
	        int page = 1;
	        do {
	            String jsonData = this.apiService.doGet(StringUtils.replace(url, "{page}", page + ""));
	            EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Item.class);
	            List<Item> items = (List<Item>) easyUIResult.getRows();
	            if (items == null || items.isEmpty()) {
	                break;
	            }
	            // 写入数据
	            this.httpSolrServer.addBeans(items);
	            page++;
	        } while (true);

	        this.httpSolrServer.commit();
	    }

	}

