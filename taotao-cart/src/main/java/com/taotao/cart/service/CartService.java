package com.taotao.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.vo.TaotaoResult;
@Service
public class CartService {

	@Autowired
	 private CartMapper cartMapper;
	public TaotaoResult saveItem(Cart newCart) {
		/**
		 *两种情况,
		 *第一,该商品在购物车没有
		 *第二,该商品在购物车中存在
		 */
		
		
		/**
		 * 先经循查询.如果商品存在于购物车,数量在原有的基础上进行加一
		 */
		Cart cart=this.cartMapper.queryCartByUserIdAndItemId(newCart.getUserId(), newCart.getItemId());
		
		/**
		 * 判断该商品是否存在于购物车中
		 */
		//不存在
		if(cart==null){
			this.cartMapper.save(newCart);
			return  TaotaoResult.ok(newCart.getId());
		}
		//存在
		else{
			
		/**
		 * 此时只需要将该商品的数量更新即可
		 */
			    cart.setNum(cart.getNum() + 1);
	            this.cartMapper.updateCartNum(cart);
	            return TaotaoResult.build(202, "该商品已经存在购物车中!商品数量+1", null);
		}
		
	}
	/**
	 * 查询购物车里的商品
	 * @param userId
	 * @return
	 */
	public TaotaoResult queryCartList(Long userId) {
		List<Cart> carts=this.cartMapper.queryCartList(userId);
		return TaotaoResult.ok(carts);
	}
	/**
	 * 更新购物车商品的数量
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	public TaotaoResult updateNum(Long userId, Long itemId, Integer num) {
		//这里直接将数量传进去,直接更改商品的数量
		 Integer count = this.cartMapper.updateCartNumByUserIdAndItemId(userId, itemId, num);
	        if (count == null || count.intValue() == 0) {
	            return TaotaoResult.build(201, "该商品不存在购物车中!");
	        }
	        return TaotaoResult.ok();
	}
	/**
	 * 删除购物车
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public TaotaoResult delete(Long userId, Long itemId) {
		 Integer count = this.cartMapper.delete(userId, itemId);
	        if (count == null || count.intValue() == 0) {
	            return TaotaoResult.build(201, "该商品不存在购物车中!");
	        }
	        return TaotaoResult.ok();
	    }
	}


