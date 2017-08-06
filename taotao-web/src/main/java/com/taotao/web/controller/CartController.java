package com.taotao.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.web.pojo.Cart;
import com.taotao.web.pojo.User;
import com.taotao.web.service.CartCookieService;
import com.taotao.web.service.CartService;
import com.taotao.web.threadlocal.UserThreadLocal;

@RequestMapping(value = "cart")
@Controller
public class CartController {
	
	
	
	  @Autowired
	    private CartService cartService;
	  @Autowired
	  CartCookieService  cartCookieService;
	@RequestMapping("add/{itemId}")
	/**
	 *将商品保存到购物车
	 */
	//用户不管是否登录,都可以将商品保存到购物车
	public String addItemToCart(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		//判断用户是否登录
		User user=UserThreadLocal.get();
		
		if(user==null){
			//将商品保存到cookie中
			
			this.cartCookieService.addItemToCart( itemId,request,response);
			
		}
		else{
			//将商品持久化
			this.cartService.addItemToCart(user, itemId);
		}
		
		return "redirect:/cart/show.html";//重定向到购物车list页面
	
	}
	
	/**
	 * 
	 * 查询购物车
	 */
	@RequestMapping("show")
	public ModelAndView showCart(){
    	//跳到cart.jsp中
    	ModelAndView mv =new ModelAndView("cart");
    	User user=UserThreadLocal.get();
    	List<Cart> carts=null;
		if (user==null) {
			//未登录
			
		}else{
			//登陆  
			carts=this.cartService.queryCartByUser(user);
		}
	    mv.addObject("cartList", carts);
		return mv;
	}
	 @RequestMapping("delete/{itemId}")
	    public String deleteCart(@PathVariable("itemId") Long itemId,HttpServletRequest request,HttpServletResponse response){
	    	  User user = UserThreadLocal.get();
	    	  if (user == null) {
	              // 未登录，从cookie中查询商品
	              try {
	              } catch (Exception e) {
	                  // TODO
	                  e.printStackTrace();
	              }
	          } else {
	              // 登录，从api查询商品
	              this.cartService.deleteCart(user, itemId);
	          }
	          return "redirect:/cart/show.html";
	      }
	    	/**
	    	 * 更新数量
	    	 */
	    @RequestMapping(value = "update/num/{itemId}/{num}")
	    @ResponseBody
	    public TaotaoResult updateCart(@PathVariable("itemId") Long itemId, @PathVariable("num") Integer num,HttpServletRequest request,HttpServletResponse response) {
	        User user = UserThreadLocal.get();
	        
	        if (user == null) {
	            // 未登录，从cookie中查询商品
	            try {
	            } catch (Exception e) {
	                e.printStackTrace();
	                return TaotaoResult.build(201, "更新数量失败！");
	            }
	        } else {
	            // 登录，从api查询商品
	            this.cartService.updateCart(user, itemId, num);
	        }
	        return TaotaoResult.ok();
	    }
	    
}
