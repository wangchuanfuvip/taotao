package com.taotao.cart.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.cart.pojo.Cart;

public interface CartMapper {

	public void save(Cart cart) ;

	/**
	 * 查询购物车
	 * @param userId
	 * @param itemId
	 * @return
	 */
    Cart queryCartByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);//多个参数要加注解 @Param

    /**
     * 更新购物车数量
     * @param cart
     */
	public void updateCartNum(Cart cart);

	/**
	 * 查询购物车
	 * @param userId
	 * @return
	 */
	public List<Cart> queryCartList(Long userId);

	/**
	 * 更新
	 * @param userId
	 * @param itemId
	 * @param num
	 * @return
	 */
	public Integer updateCartNumByUserIdAndItemId(@Param ("userId")Long userId, @Param("itemId")Long itemId, @Param("num")Integer num);

	/**
	 * 删除购物车商品
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public Integer delete(@Param("userId") Long userId, @Param("itemId") Long itemId);

}
