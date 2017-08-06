/**
 * @author wangchuanfu
 * 2017年4月17日
 */
package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.vo.EasyUIResult;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

/**
 * @author fufu
 *
 */
@Service
public class ItemService  extends BaseService <Item>{

	/**
	 * @author wangchuanfu
	 * 2017年4月18日
	 */
    private static final ObjectMapper MAPPER = new ObjectMapper();

	//Spriing 事物的传播性
	@Autowired 
	ItemDescService   itemDescService;
	@Autowired 
	ItemMapper   itemMapper;
	 @Autowired
	  private ApiService apiService;
	 @Autowired
     private ItemParamItemService itemParamItemService;
	 @Autowired
	    private RabbitTemplate rabbitTemplate;
	public void saveItem(Item item, String desc,String itemParams) {
		super.save(item);
		 ItemDesc itemDesc =new ItemDesc();
		 itemDesc.setItemId(item.getId());
		 itemDesc.setItemDesc(desc);
		 itemDesc.setUpdated(new Date());
		 itemDesc.setCreated(itemDesc.getUpdated());
		 this.itemDescService.save(itemDesc);
		 // 保存商品规格数据
        ItemParamItem itemParamItem =new ItemParamItem ();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(itemParamItem.getCreated());
        /**
         * 利用rabbitmq进行数据更新
         */
        
        //this.rabbitTemplate.convertAndSend("insert", item.getId());
        /**
         * 利用接口进行数更新
         */
        this.itemParamItemService.save(itemParamItem);
		
	}
	/**
	 * @author wangchuanfu
	 * 2017年4月20日
	 */
	public EasyUIResult queryList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows,true); 
		
		List <Item> items=new ArrayList<Item>();
		items =this.itemMapper.queryListItemByUpdated();
	    PageInfo <Item> pageInfo=new   PageInfo<Item>(items);
	    
		return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
		
	}
	
	/**
	 * @author wangchuanfu
	 * 2017年4月20日
	 */
	public void updateItem(Item item, String desc,String itemParams) {
		 item.setUpdated(new Date());
	     item.setCreated(null);//强制设置为null
	     super.updateSelective(item);
	   //更新商品描述数据
	     ItemDesc itemDesc = new ItemDesc();
	     itemDesc.setUpdated(new Date());
	     itemDesc.setItemId(item.getId());
	     itemDesc.setItemDesc(desc);
	     this.itemDescService.updateSelective(itemDesc);
	     
	     
	     //更新规格参数数据
	     ItemParamItem itemParamItem = new ItemParamItem();
	     itemParamItem.setItemId(item.getId());
	     itemParamItem.setParamData(itemParams);
	     this.itemParamItemService.updateParamByItemId(itemParamItem);
	     
	     /**
	      * 后台提供接口,用于redis缓存的更新,以及Solr数据的更新
	      */
	     String url="http://www.taotao.com/service/item/updateRedis/"+item.getId();//这个方法最终会调到taotao-web ItemController中的updateRedis();
	     try {
			this.apiService.doGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	     String searchUrl="http://search.taotao.com/update";//这个方法会调到taotao-search中的update();
	     
	     try {
	    		this.apiService.doPostJson(searchUrl, MAPPER.writeValueAsString(item));
			} catch (Exception e) {
				e.printStackTrace();
			}
	   //  this.rabbitTemplate.convertAndSend("update", item.getId());

		
	}
	

}
