package com.taotao.web.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.util.CookieUtils;
import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.Item;


@Service
public class CartCookieService {
	 private static final ObjectMapper MAPPER = new ObjectMapper();

	    private static final Integer COOKIE_TIME = 60 * 60 * 24 * 30;
	 private static final String TT_CART = "TAOTAO_CART";
	 @Autowired
	    private ItemService itemService;
	public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {

	
		/**
		 * 先查Cookie购物车中商品的数据,如果存在+1,json 的格式保存数据
		 */
		  String cookieData = CookieUtils.getCookieValue(request, TT_CART);
		  /**
		   * cookie中不存在该商品
		   */
	       List<Cart> carts = null;
		if(StringUtils.isEmpty(cookieData)){
		    // 将商品数据保存到cookie中
            carts = new ArrayList<Cart>(1);
            carts.add(itemToCart(itemId));
		}else{
			 carts = queryCartByUser(request, response, false);
			
			/**
			 * 循环遍历
			 */
			 Boolean bool=false;
			  Cart cart = null;
			 for (Cart c : carts) {
				 /**
				  * 先判断该商品是否存在于Cookie中
				  */
				 ///实名存在.则数量+1
				if( (c.getItemId().intValue())==(itemId.intValue())){
					bool=true;
					  cart = c;
					break;
				};
			}
			  if (bool) {
	                // 该商品存在于Cookie中,数量+1
				  cart.setNum(cart.getNum() + 1);
	            } else {
	            	//该商品不存在Cookie中,将此商品加入Cookie中
	                carts.add(itemToCart(itemId));
	            }
		}
        CookieUtils.setCookie(request, response, TT_CART, MAPPER.writeValueAsString(carts), COOKIE_TIME);

		
	}



	private Cart itemToCart(Long itemId) throws Exception {
		/**
		 * 调用后台接口,查询出后台商品数据
		 */
		  Item item = this.itemService.queryItemById(itemId);
		/**
		 * 封装参数
		 */
		  Cart cart = new Cart();
	        cart.setItemId(itemId);
	        String[] images = item.getImages();
	        if (images == null) {
	            cart.setItemImage("");
	        } else {
	            cart.setItemImage(images[0]);
	        }
	        cart.setItemPrice(item.getPrice());
	        // 编码
	        String title = URLEncoder.encode(item.getTitle(), "UTF-8");
	        cart.setItemTitle(title);
	        cart.setNum(1);
	        cart.setCreated(new Date());
	        return cart;
	}


	   public List<Cart> queryCartByUser(HttpServletRequest request, HttpServletResponse response,
	            boolean isDecode) throws Exception {
	        // 读取cookie中购物车的数据
	        String cookieData = CookieUtils.getCookieValue(request, TT_CART);
	        if (null == cookieData) {
	            return null;
	        }
	        List<Cart> carts = MAPPER.readValue(cookieData,
	                MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
	        if (isDecode) {
	            for (Cart cart : carts) {
	                String title = URLDecoder.decode(cart.getItemTitle(), "UTF-8");
	                cart.setItemTitle(title);
	            }
	        }
	        return carts;
	    }

	    public void deleteCart(Long itemId, HttpServletRequest request, HttpServletResponse response)
	            throws Exception {
	        List<Cart> carts = queryCartByUser(request, response, false);
	        for (int i = 0; i < carts.size(); i++) {
	            Cart cart = carts.get(i);
	            if (cart.getItemId().intValue() == itemId.intValue()) {
	                carts.remove(i);
	                break;
	            }
	        }
	        // 写入到cookie中
	        CookieUtils.setCookie(request, response, TT_CART, MAPPER.writeValueAsString(carts), COOKIE_TIME);
	    }

	    public void updateCart(HttpServletRequest request, HttpServletResponse response, Long itemId, Integer num)throws Exception {
	        List<Cart> carts = queryCartByUser(request, response, false);
	        for (int i = 0; i < carts.size(); i++) {
	            Cart cart = carts.get(i);
	            if (cart.getItemId().intValue() == itemId.intValue()) {
	                cart.setNum(num);
	                break;
	            }
	        }
	        // 写入到cookie中
	        CookieUtils.setCookie(request, response, TT_CART, MAPPER.writeValueAsString(carts), COOKIE_TIME);
	    }

	}

