/**
 * @author wangchuanfu
 * 2017年4月20日
 */
package com.taotao.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.pojo.ItemParamItem;

/**
 * @author fufu
 *
 */
@Service
public class ItemParamItemService  extends BaseService<ItemParamItem>{
	@Autowired
    private ItemParamItemMapper itemParamItemMapper;
	public void updateParamByItemId(ItemParamItem itemParamItem) {
		 this.itemParamItemMapper.updateParamByItemId(itemParamItem);		
	}

}
