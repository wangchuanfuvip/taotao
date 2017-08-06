package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping("/content/category")
@Controller
public class ContentCategoryController {
	 @Autowired
	    private ContentCategoryService contentCategoryService;
	 @RequestMapping("/list")
	    @ResponseBody
	    public List<ContentCategory> queryList(@RequestParam(value = "id", defaultValue = "0") Long id) {
	        ContentCategory contentCategory = new ContentCategory();
	        contentCategory.setParentId(id);
	        return this.contentCategoryService.queryListByWhere(contentCategory);
	    }

	 /**
	  * 创建节点
	  */
	 @RequestMapping("/create")
	    @ResponseBody
	 public TaotaoResult createNode(@RequestParam("parentId") long parentId,@RequestParam("name") String name){
	        ContentCategory contentCategory = this.contentCategoryService.createNode(parentId, name);
	        return TaotaoResult.ok(contentCategory);// 返回
          }
	 /**
	  * 更新节点
	  * 重命名
	  * @param contentCategory
	  * @return
	  */
	 @RequestMapping("/update")
	 @ResponseBody
	 public TaotaoResult updateeNode(ContentCategory contentCategory){
		
		 this.contentCategoryService.updateNode(contentCategory);
	        return TaotaoResult.ok();// 返回
       }
	 /**删除
	  * 
	  */
	 @RequestMapping("/delete")
	 @ResponseBody
	 
	   public  TaotaoResult deleteNode(@RequestParam("parentId") long parentId, @RequestParam("id") Long id){
		 this.contentCategoryService.deleteNode(parentId,id);
	        return TaotaoResult.ok();// 返回
	 }
	 
	 
	 }
