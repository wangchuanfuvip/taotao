/**
 * @author wangchuanfu
 * 2017年4月20日
 */
package com.taotao.manage.mapper;

import com.taotao.manage.base.mapper.TaotaoMapper;
import com.taotao.manage.pojo.ItemParamItem;

/**
 * @author fufu
 *
 */
public interface ItemParamItemMapper extends TaotaoMapper<ItemParamItem>{

	void updateParamByItemId(ItemParamItem itemParamItem);

}
