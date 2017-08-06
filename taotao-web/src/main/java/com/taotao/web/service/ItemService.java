package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.RedisService;
import com.taotao.common.spring.exetend.PropertyConfig;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.pojo.Item;

@Service
public class ItemService {
	@Autowired
	private ApiService apiService;
	@Autowired
	private RedisService redisService;

	  private static final String REDIS_ITEM_KEY = "TAOTAO_WEB_ITEM_";
	  private static final String REDIS_ITEM_DESC_KEY = "TAOTAO_WEB_ITEM_DESC_";
	  private static final String REDIS_ITEM_PARAM_ITEM_KEY = "TAOTAO_WEB_ITEM_PARAM_ITEM_";
	private static final ObjectMapper MAPPER = new ObjectMapper();
	   @PropertyConfig
	  private String MANAGE_TAOTAO;
	@SuppressWarnings("unused")
	public Item queryItemById(Long itemId) {
    	//String key =ITEM_KEY+itemId;
    	String key =REDIS_ITEM_KEY+itemId;
    	//从缓存中命中
	    try {
	    	String redisData=this.redisService.get(key);
	    	 //先判断redisData是否为空
	    if (StringUtils.isNoneEmpty(redisData)) {
			//返回Item对象
				return MAPPER.readValue(redisData, Item.class);
			} 
	    } catch (Exception e) {
			e.printStackTrace();
			}
    	// 数据从后台管理系统中获取，通过Httpclient获取
        String url = MANAGE_TAOTAO + "/item/query/" + itemId;
        Item item = null;
        try {
          String jsonData = this.apiService.doGet(url);
          //把json格式的数据转为item对象
          TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, Item.class);
         item= (Item) taotaoResult.getData();
        } catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        //写入缓存中
        if (null!=item) {
        	//将Item  
			try {                        // 转为json格式的数据,并保存在redis中
				this.redisService.set(key, MAPPER.writeValueAsString(item),60 * 60 *10);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return item;
	
    }

	/**
	 * 获取商品描述信息
	 * 
	 * @param itemId
	 * @return
	 * @author wangchuanfu
	 */
	@SuppressWarnings("unused")
	public ItemDesc queryItemDescById(Long itemId) {
		String key =REDIS_ITEM_DESC_KEY+itemId;
		/**
		 * 查询商品数据
		 * @author wangchuanfu
		 */
		//String url = "http://localhost:8081/item/query/item/desc/" + itemId;
		String url=MANAGE_TAOTAO+ itemId;//
		ItemDesc itemDesc = null;
		String jsonData;
		/**
		 * 先从缓存中取数据,
		 * 如果不为空返回json格式的数据
		 */
		String redisData = this.redisService.get(key);
	 
	  /**
	   * 此时会产生异常,
	   * 我们必须将其捕获,
	   * 否则前端给用户展示会非常怪
	   */
	  
		if (StringUtils.isNotEmpty(redisData)) {
			try {
			
				return MAPPER.readValue(redisData, ItemDesc.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			jsonData = this.apiService.doGet(url);
			/**
			 * 将json数据转成Item对象
			 */
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, ItemDesc.class);
			itemDesc=(ItemDesc) taotaoResult.getData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 如果查询的商品数据不为空,将其放入缓存中
		 * 
		 */
		  //将Item写入缓存中
          
			try {                        // 转为json格式的数据,并保存在redis中
				this.redisService.set(key, MAPPER.writeValueAsString(itemDesc),60 * 60 * 24 * 10);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return itemDesc;

	}

	/**
	 * 获取商品的规格参数信息
	 * 
	 * @param itemId
	 * @return
	 */
	public String queryItemParamById(Long itemId) {
		// String url =  "http://localhost:8081/item/param/item/query/" + itemId;
		String url=MANAGE_TAOTAO+itemId;
		String key = REDIS_ITEM_PARAM_ITEM_KEY + itemId;
		   String strResult = null;
			//从缓存中命中
			    try {
			    	String redisData=this.redisService.get(key);
			    	 //先判断redisData是否为空
			         if (StringUtils.isNotEmpty(redisData)) {
//						return MAPPER.readValue(redisData);
						 return redisData;
					} 
			    } catch (Exception e) {
					e.printStackTrace();
					}
			  
			  try {
		          String jsonData = this.apiService.doGet(url);
		          //把json格式的数据转为ItemParamItem对象
		          TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, ItemParamItem.class);
		          ItemParamItem itemParamItem = (ItemParamItem) taotaoResult.getData();
		          String paramData = itemParamItem.getParamData();
		          //先转成json串
		          ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);
		          //SringBuilder 封装数组 进行遍历
		          StringBuilder sb = new StringBuilder();
		          
		          sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
		          //遍历数组
		          for (JsonNode jsonNode : arrayNode) {
		        	  sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + jsonNode.get("group").asText()
		                        + "</th></tr><tr>");
		        	  ArrayNode params = (ArrayNode)  jsonNode.get("params");
		        	  for (JsonNode param : params) {
						sb.append("<tr><td class=\"tdTitle\">"+param.get("k").asText()+"</td><td>"
                         + param.get("v").asText() + "</td></tr>");
					}
				}
		          sb.append("</tbody></table>");		          
		          strResult= sb.toString();
		        } catch (Exception e) {
					e.printStackTrace();
				}
			//写入缓存中
		        if (null!=strResult) {
		        	//  转为json格式的数据,并保存在redis中
					try {
						this.redisService.set(key, strResult,60 * 60 * 24 * 10);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return strResult;
	}

	
		public void updateRedis(Long itemId) {
			this.redisService.del(REDIS_ITEM_KEY + itemId);
			this.redisService.del(REDIS_ITEM_DESC_KEY + itemId);
			this.redisService.del(REDIS_ITEM_PARAM_ITEM_KEY + itemId);
			
	}

}
