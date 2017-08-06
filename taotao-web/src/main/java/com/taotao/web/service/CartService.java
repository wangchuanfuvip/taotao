package com.taotao.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.spring.exetend.PropertyConfig;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.Item;
import com.taotao.web.pojo.User;
@Service
public class CartService {

	
	
	 @Autowired
     private ItemService itemService;
	 @PropertyConfig
     private String CART_TAOTAO;
	   @Autowired
	     private ApiService apiService;

	     private static final ObjectMapper MAPPER = new ObjectMapper();
	public Boolean addItemToCart(User user, Long itemId) {
	
		//调用后台查询商品
		Item item=this.itemService.queryItemById(itemId);
		
		/**
		 * 调用购物车系统将数据保存到数据库
		 */
		String url=CART_TAOTAO + "/cart/save";
		
		Map<String,String> params=new HashMap <String,String> ();
		  params.put("userId", String.valueOf(user.getId()));
	        params.put("itemId", String.valueOf(itemId));
	        params.put("itemTitle", item.getTitle());
	        String[] images = item.getImages();
	        if (null == images) {
	            params.put("itemImage", "");
	        } else {
	            params.put("itemImage", images[0]);
	        }
	        params.put("itemPrice", String.valueOf(item.getPrice()));
	        params.put("num", "1");// 默认为：1
		try {
			 String jsonData =apiService.doPost(url, params, "UTF-8");
			  JsonNode jsonNode = MAPPER.readTree(jsonData);
			  Integer status = jsonNode.get("status").intValue();
	            if (status == 200 || status == 202) {
	                return true;
	            }
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	public List<Cart> queryCartByUser(User user) {

		 String url = CART_TAOTAO + "/cart/query/" + user.getId();
		 try {
	            String jsonData = this.apiService.doGet(url);
	            TaotaoResult taotaoResult = TaotaoResult.formatToList(jsonData, Cart.class);
	            return (List<Cart>) taotaoResult.getData();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	}
	public Boolean deleteCart(User user, Long itemId) {
		/**
		 * 调用购物车的删除		
		 */
		  String url = CART_TAOTAO + "/cart/delete/" + user.getId() + "/" + itemId;
	        try {
	            String jsonData = this.apiService.doPost(url, null);
	            JsonNode jsonNode = MAPPER.readTree(jsonData);
	            Integer status = jsonNode.get("status").intValue();
	            if (status == 200) {
	                return true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	}
	
	/**
	 * 调用购物车的更新		
	 */
	 public Boolean updateCart(User user, Long itemId, Integer num) {
		
		  String url = CART_TAOTAO + "/cart/update/num/" + user.getId() + "/" + itemId + "/" + num;
	        try {
	            String jsonData = this.apiService.doPost(url, null);
	            JsonNode jsonNode = MAPPER.readTree(jsonData);
	            Integer status = jsonNode.get("status").intValue();
	            if (status == 200) {
	                return true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	    
	}

}
