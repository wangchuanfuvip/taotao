package com.taotao.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.pojo.Item;
import com.taotao.web.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
	/**
	 * 取出数据
	 */
	@Autowired
	private ItemService itemService;

	@RequestMapping("{itemId}")
	public ModelAndView queryItem(@PathVariable("itemId") Long itemId) {
		ModelAndView mv = new ModelAndView("item");
		Item item = null;
		/**
		 * 查询商品的信息
		 */

		// 增加代码的健壮性
		item = itemService.queryItemById(itemId);

		mv.addObject("item", item);
		/**
		 * 查询商品描述信息
		 */
		ItemDesc itemDesc = itemService.queryItemDescById(itemId);
		mv.addObject("itemDesc", itemDesc);
		/**
		 * 查询商品的规格参数信息 之所以返回一个String 是因为返回值是一个table,在service层进行拼接table,在jsp
		 * 中通过EL表达式取得值是 {itemParam}
		 * 
		 */
		String itemParam = this.itemService.queryItemParamById(itemId);
		mv.addObject("itemParam", itemParam);
		return mv;
	}

	/**
	 * 用于redis的更新
	 */
	@RequestMapping(" updateRedis/{itemId}")
	@ResponseBody
	public TaotaoResult updateRedis(@PathVariable("itemId") Long itemId) {

		/**
		 * 更新redis数据,其实就是将之前的数据删掉,
		 */
		this.itemService.updateRedis(itemId);
		return TaotaoResult.ok();
	}

}
