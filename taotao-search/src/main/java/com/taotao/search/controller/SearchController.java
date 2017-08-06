package com.taotao.search.controller;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.SearchService;
@Controller
public class SearchController {
	   @Autowired
	    private HttpSolrServer httpSolrServer;
	/**
	 * 通过关键字进行搜索
	 */
	    @Autowired
	    private SearchService searchService;
	    @RequestMapping(value="search",method=RequestMethod.POST)
	     
	    @ResponseBody
	    public TaotaoResult search(@RequestParam("keyWords") String keyWords,@RequestParam("page") Integer page,
	    @RequestParam("rows") Integer rows){
	        return this.searchService.search(keyWords,page,rows);
	     }
	    /**
	     * 更新solr中的数据
	     * @param item
	     * @return
	     */

	    @RequestMapping(value = "/update", method = RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult update(@RequestBody Item item){
	        return this.searchService.update(item);
	    
	  }
}
