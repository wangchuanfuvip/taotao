/**
 * @author wangchuanfu
 * 2017年4月18日
 */
package com.taotao.manage.pojo;

import java.util.Date;

/**
 * @author fufu
 *
 */
public class BasePojo {
	private Date updated;
	private Date created;
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
}
