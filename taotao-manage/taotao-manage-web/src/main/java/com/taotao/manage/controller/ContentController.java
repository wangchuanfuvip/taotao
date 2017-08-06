package com.taotao.manage.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.vo.EasyUIResult;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@RequestMapping("/content")
@Controller
public class ContentController {
	/**
	 * 查询
	 */
	 @Autowired
	    private ContentService contentService;
	 @RequestMapping("/query/list")
	    @ResponseBody
	    public EasyUIResult queryList(@RequestParam("categoryId") Long categoryId,
	            @RequestParam("page") Integer page, @RequestParam("rows") Integer rows){
		 return this.contentService.queryList(categoryId, page, rows);
	 }
	    /**
	     * 新增
	     */
	 @RequestMapping("/save")
	    @ResponseBody
	    public TaotaoResult save(Content content) {
	        // 设置实体数据
	        content.setCreated(new Date());
	        content.setUpdated(content.getCreated());
	        this.contentService.save(content);
	        return TaotaoResult.ok();
	    }
	  /**
	     * 编辑
	     */
	 @RequestMapping("/edit")
	    @ResponseBody
	    public TaotaoResult edit(Content content) {
	        // 设置实体数据
	        content.setCreated(new Date());
	        content.setUpdated(content.getCreated());
	        content.setUrl(content.getUrl());
	        content.setTitleDesc(content.getTitleDesc());
	        content.setId(content.getId());
	        
	        this.contentService.update(content);
	        return TaotaoResult.ok();
	    }
	 
	 /**
	  * 删除
	  * String ids = null;
		if (request.getParameter("ids") != null) {
			ids = request.getParameter("ids").trim();

			String[] idArr = ids.split(",");
			for (int i = 0; i < idArr.length; i++) {
				PharmacistComment bean = new PharmacistComment();
				bean.setPharmacistCommentId(Long.parseLong(idArr[i]));
				bean.setIsDelete("Y");
				pharmacistCommentService.update(bean);

			}
		}
	  */
	
	 @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult delete(@RequestParam("ids") String ids,Content content,HttpServletRequest request,
				HttpServletResponse response) {
		 String[] idList = ids.split(",");
		 for (int i = 0; i < idList.length; i++) {
				this.contentService.deleteById(Long.valueOf(idList[i]));
			}
	        return TaotaoResult.ok();
	    }
}
