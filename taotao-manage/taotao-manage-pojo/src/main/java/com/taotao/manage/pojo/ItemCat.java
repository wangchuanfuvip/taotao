/**
 * @author wangchuanfu
 * 2017年4月16日
 */
package com.taotao.manage.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author fufu
 *
 */
@Table(name = "tb_item_cat")
public class ItemCat {
	
	//主键

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	//父id
    // 父类目ID=0时，代表的是一级的类目
    @Column(name = "parent_id")
	private Long parentId;
	//名称
	private String name;
	private Integer status;
	@Column(name = "sort_order")
	private Integer sortOrder;
    @Column(name = "is_parent")
	private Boolean isParent;
	private Date updated;
	private Date created;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}


	
	@Override
	public String toString() {
		return "ItemCat [id=" + id + ", parentId=" + parentId + ", name="
				+ name + ", status=" + status + ", sortOrder=" + sortOrder
				+ ", isParent=" + isParent + ", updated=" + updated
				+ ", created=" + created + "]";
	}
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
	public String getText(){
		return getName();
	}
	
	public String getState(){
		return getIsParent() ? "closed":"open";
	}
	
}
