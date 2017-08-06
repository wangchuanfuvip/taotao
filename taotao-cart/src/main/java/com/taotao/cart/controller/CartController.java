package com.taotao.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import com.taotao.common.vo.TaotaoResult;

@Controller
@RequestMapping(value = "cart")

public class CartController {

	@Autowired
	CartService cartService;

	@RequestMapping(value = "save", method = RequestMethod.POST)

	public TaotaoResult saveItem(Cart cart) {

		return cartService.saveItem(cart);

	}

	/**
	 * 根据用户的id查询商品
	 */
	@RequestMapping(value = "query/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult queryCartList(@PathVariable("userId") Long userId) {
		return this.cartService.queryCartList(userId);

	}

	/**
	 * 更新商品数量
	 * 
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping(value = "update/num/{userId}/{itemId}/{num}", method = RequestMethod.POST)
	@ResponseBody
	// 传入userId与itemId 会走索引 效率高
	public TaotaoResult updateNum(@PathVariable("userId") Long userId, @PathVariable("itemId") Long itemId,
			@PathVariable("num") Integer num) {
		return this.cartService.updateNum(userId, itemId, num);
	}

	/**
	 * 删除购物车中的商品数据
	 * 
	 * @param userId
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "delete/{userId}/{itemId}", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult delete(@PathVariable("userId") Long userId, @PathVariable("itemId") Long itemId) {
		return this.cartService.delete(userId, itemId);
	}

}
