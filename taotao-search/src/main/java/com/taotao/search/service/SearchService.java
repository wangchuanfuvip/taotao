package com.taotao.search.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.search.pojo.Item;
@Controller
public class SearchService {

	  @Autowired
	    private HttpSolrServer httpSolrServer;
	    public TaotaoResult search(String keyWords,Integer page,Integer rows) {
	
	    	//构造搜索对象
	    SolrQuery solrQuery=new SolrQuery();
	    solrQuery.setQuery(keyWords);
	    	//分页数据
	    solrQuery.setStart((Math.max(1, page) - 1) * rows);
        solrQuery.setRows(rows);
	    	
	    	
	    	//显示高亮
        // 设置高亮显示
        solrQuery.setHighlight(true);// 开启高亮
        solrQuery.setHighlightSimplePre("<span class=\"red\">");
        solrQuery.setHighlightSimplePost("</span>");
        solrQuery.addHighlightField("title");
        
        /**
         * 进行查询
         * 
         */
        
        try {
			QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);
			List<Item> items = queryResponse.getBeans(Item.class);
			//判断  是否查到数据
			if (items==null || items.isEmpty()) {
				return TaotaoResult.build(200, "没有搜到数据");
			}
			// 将高亮的标题数据写回到数据对象中
			Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
			
			for (Map.Entry<String, Map<String, List<String>>> highlighting : map.entrySet()) {
				
				for (Item item : items) {
					
					 if (!highlighting.getKey().equals(item.getId().toString())) {
			             continue;
			         }
			         item.setTitle(StringUtils.join(highlighting.getValue().get("title"), ""));
			         break;
				}
			}
			return TaotaoResult.build(200, String.valueOf(queryResponse.getResults().getNumFound()), items);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return TaotaoResult.build (201,"没有搜到数据");
	}
		public TaotaoResult update(Item item) {
			 try {
		            this.httpSolrServer.addBean(item);
		            this.httpSolrServer.commit();
		            return TaotaoResult.ok();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        return TaotaoResult.build(201, "更新solr数据失败!");
		    }

}
