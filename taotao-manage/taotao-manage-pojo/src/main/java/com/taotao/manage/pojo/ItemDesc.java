/**
 * @author wangchuanfu
 * 2017年4月18日
 */
package com.taotao.manage.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author fufu
 *
 */
@Table(name = "tb_item_desc")
public class ItemDesc extends BasePojo{
	 @Id//主键
	    @Column(name = "item_id")
	    private Long itemId;
	    @Column(name = "item_desc")
	    private String itemDesc;
	    public Long getItemId() {
	        return itemId;
	    }
	    public void setItemId(Long itemId) {
	        this.itemId = itemId;
	    }
	    public String getItemDesc() {
	        return itemDesc;
	    }
	    public void setItemDesc(String itemDesc) {
	        this.itemDesc = itemDesc;
	    }
		@Override
		public String toString() {
			return "ItemDesc [itemId=" + itemId + ", itemDesc=" + itemDesc
					+ "]";
		}
	    
}
