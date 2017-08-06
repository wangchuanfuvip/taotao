package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ContentCategory;


@Service
public class ContentCategoryService extends  BaseService<ContentCategory> {
/**
 * 新增节点
 * @param parentId
 * @param name
 * @return
 */
	public ContentCategory createNode(Long parentId, String name) {
		/**
		 * 封装参数
		 */
		 ContentCategory contentCategory = new ContentCategory();
	        contentCategory.setIsParent(false);
	        contentCategory.setName(name);
	        contentCategory.setParentId(parentId);
	        contentCategory.setStatus(1);
	        contentCategory.setUpdated(new Date());
	        contentCategory.setCreated(contentCategory.getUpdated());
	        save(contentCategory);
	        // 修改父节点的isParent为true
	        ContentCategory categoryParent = queryById(parentId);
	        if (!categoryParent.getIsParent()) {
	            categoryParent.setIsParent(true);
	            categoryParent.setUpdated(new Date());
	            updateSelective(categoryParent);
	        }
		return contentCategory;
	  }
	 /**
     * 修改节点名称
     * @param contentCategory
     */
	public void updateNode(ContentCategory contentCategory) {
		// TODO Auto-generated method stub
		 contentCategory.setUpdated(new Date());
		 super.updateSelective(contentCategory);		
	}
	/**
	 * 删除节点
	 * @param parentId
	 * @param id
	 */
	public void deleteNode(long parentId, Long id) {
		//删除当前节点
		super.deleteById(id);
		//;判断父节点是否有子节点
		//先拿到父节点
		ContentCategory contentCategory = super.queryById(parentId);
		//通过父节点的id作为parentId 查找所有子节点
		 ContentCategory param = new ContentCategory();
		 param.setParentId(contentCategory.getId());
		 List<ContentCategory> contentCategories = super.queryListByWhere(param);
		 //判断父节点是否为空
		 if (contentCategories.isEmpty()) {
			 contentCategory.setIsParent(false);
			 contentCategory.setUpdated(new Date());
	         super.updateSelective(contentCategory);//更新
		}
		 //删除当前节点的子节点
		 List<Long> ids=new ArrayList<Long>();
		 findByParentId(ids, id);
		   if(!ids.isEmpty()){
			   /**
			    * 循环删除
			    */
	            super.deleteByIds(ids.toArray(new Long[]{}));
	        }
	}
	   /**
     * 递归查找所有待删除的id
     * @param ids
     * @param id
     */
	private void findByParentId(List<Long> ids, Long id) {
			ContentCategory  param =new ContentCategory();
			 param.setParentId(id);
			 List<ContentCategory> contentCategories = super.queryListByWhere(param);
		  //判断
			 if (!contentCategories.isEmpty()) {
				for (ContentCategory contentCategory : contentCategories) {
					 ids.add(contentCategory.getId());
					if (contentCategory.getIsParent()) {
						 findByParentId(ids, contentCategory.getId());
					}
				}
			}
	   } 
}


