/**
 * @author wangchuanfu
 * 2017年4月16日
 */
package com.taotao.manage.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemCatService;
import com.taotao.manage.service.ItemParamService;

/**
 * @author fufu
 *
 */
@RequestMapping("/item/param")
@Controller
public class ItemParamController {
	
	@Autowired
	private ItemParamService itemParamService;
	 @RequestMapping("/list")
	 @ResponseBody
	  public EasyUIResult queryList(@RequestParam("page") Integer page, @RequestParam("rows") Integer rows) {
	        PageHelper.startPage(page, rows);
	        List<ItemParam> itemParams = this.itemParamService.queryAll();
	        PageInfo<ItemParam> pageInfo = new PageInfo<ItemParam>(itemParams);
	        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
	    }
	 
	 @RequestMapping("/query/itemcatid/{itemCatId}")
	    @ResponseBody
	    public TaotaoResult queryItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId) {
	        ItemParam itemParam = new ItemParam();
	        itemParam.setItemCatId(itemCatId);
	        return TaotaoResult.ok(this.itemParamService.queryByWhere(itemParam));
	    }

	    @RequestMapping("/save/{itemCatId}")
	    @ResponseBody
	    public TaotaoResult saveItemParam(@PathVariable("itemCatId") Long itemCatId,
	            @RequestParam("paramData") String paramData) {
	        ItemParam itemParam = new ItemParam();
	        itemParam.setItemCatId(itemCatId);
	        itemParam.setParamData(paramData);
	        itemParam.setCreated(new Date());
	        itemParam.setUpdated(itemParam.getCreated());

	        this.itemParamService.save(itemParam);
	        return TaotaoResult.ok();
	    }
		/**
		 * 删除
		 */

		@RequestMapping(value = "/delete", method = RequestMethod.POST)
		@ResponseBody
		public TaotaoResult deleteItem(@RequestParam("ids") String ids) {
			String[] idList = ids.split(",");

			for (int i = 0; i < idList.length; i++) {
				this.itemParamService.deleteById(Long.valueOf(idList[i]));
			}

			// this.itemService.deleteByIds(ids.split(","));
			return TaotaoResult.ok();
		}
}
