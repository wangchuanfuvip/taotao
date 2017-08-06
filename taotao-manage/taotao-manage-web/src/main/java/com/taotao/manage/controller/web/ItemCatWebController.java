package com.taotao.manage.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.ItemCatResult;
import com.taotao.manage.service.ItemCatService;
@Controller
@RequestMapping("/web/itemcat")
public class ItemCatWebController {
	    @Autowired
	    private ItemCatService itemCatService;
	    
	/**
	 * 对外  提供接口
	 */
	@RequestMapping("/all")
	@ResponseBody
	public ItemCatResult queryAll(){
		
		return this.itemCatService.queryItemCatWebAll();
	}
}
