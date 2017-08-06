/**
 * @author wangchuanfu
 * 2017年4月18日
 */
package com.taotao.manage.mapper;

import java.util.List;

import com.taotao.manage.base.mapper.TaotaoMapper;
import com.taotao.manage.pojo.Item;

/**
 * @author fufu
 *
 */
public interface ItemMapper extends TaotaoMapper<Item> {
	
	/**
	 * 
	 */

	public List<Item> queryListItemByUpdated();

	/**
	 * @author wangchuanfu
	 * 2017年4月20日
	 */
	public void updateItem(Item item, String desc);
}
