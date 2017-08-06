package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;

@Controller
public class IndexController {

	 @Autowired
    private IndexService indexService;
	 
   
    /**
     * 首页
     * 
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        // 获取大广告位数据
        String indexAD1=this.indexService.queryIndexAD1();
       mv.addObject("indexAD1",indexAD1 );
        return mv;
    }
}
