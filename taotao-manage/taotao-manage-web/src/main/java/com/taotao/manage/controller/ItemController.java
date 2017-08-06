/**
 * @author wangchuanfu
 * 2017年4月17日
 */
package com.taotao.manage.controller;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.EasyUIResult;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemDescService;
import com.taotao.manage.service.ItemService;

/**
 * @author fufu
 *
 */
@Controller
@RequestMapping("/item")
public class ItemController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ItemController.class);
	@Autowired
	ItemService itemService;
	@Autowired
	ItemDescService itemDescService;

	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult saveItem(Item item, @RequestParam("desc") String desc,@RequestParam("itemParams") String itemParams){

		/**
		 * 代码的健壮性
		 */
		try {
			item.setUpdated(new Date());
			item.setCreated(item.getUpdated());
			item.setStatus(1);
			item.setId(null);
			LOGGER.info("新增商品数据" + item.getCid() + "商品数据的title"
					+ item.getTitle());
			// 判断是否为debug级别
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info(item.toString());
			}
			/**
			 * 
			 this.itemService.save(item); ItemDesc itemDesc =new ItemDesc();
			 * itemDesc.setItemId(item.getId()); itemDesc.setItemDesc(desc);
			 * itemDesc.setUpdated(new Date());
			 * itemDesc.setCreated(itemDesc.getUpdated());
			 * this.itemDescService.save(itemDesc);
			 */
			/**
			 * 保证两个save在同一个事物中,下面进行方法的改造
			 */
			this.itemService.saveItem(item, desc,itemParams);
		} catch (Exception e) {
			LOGGER.error("保存失败" + item.getTitle(), e);
			e.printStackTrace();
			return TaotaoResult.build(500, "异常发生,保存失败");
		}

		return TaotaoResult.ok();

	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIResult queryList(@RequestParam("page") Integer page,
			@RequestParam("rows") Integer rows) {

		return this.itemService.queryList(page, rows);

	}

	/**
	 * 删除
	 */

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult deleteItem(@RequestParam("ids") String ids) {
		String[] idList = ids.split(",");

		for (int i = 0; i < idList.length; i++) {
			this.itemService.deleteById(Long.valueOf(idList[i]));
		}

		// this.itemService.deleteByIds(ids.split(","));
		return TaotaoResult.ok();
	}

	/**
	 * 根据商品ID查询商品描述数据
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "query/item/desc/{itemId}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult queryItemDescByItemId(
			@PathVariable("itemId") long itemId) {
		ItemDesc itemDesc = this.itemDescService.queryById(itemId);
		return TaotaoResult.ok(itemDesc);
	}
	
	/**
	 * 更新
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public TaotaoResult updateItem(
			Item item , @RequestParam("desc") String desc,@RequestParam("itemParams") String itemParams) {
	   this.itemService.updateItem(item,desc,itemParams);
		return TaotaoResult.ok();
	}
	/**
	 * 给前台调用的接口
	 * 根据ID查询商品信息数据
	 */
	@RequestMapping(value = "/query/{id}")
	@ResponseBody
   public TaotaoResult queryList(@PathVariable("id") long id){
    	
    	try {
			Item item = this.itemService.queryById(id);
			//判断
			if (item.getId()!=null && item!=null ) {
				return TaotaoResult.ok(item);
			}
		} catch (Exception e) {
			return TaotaoResult.build(202, "查询失败! id = " + id);
		}
    	return TaotaoResult.build(202, "查不到商品! id = " + id);
    }
	
}
