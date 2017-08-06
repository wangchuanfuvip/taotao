/**
 * @author wangchuanfu
 * 2017年4月16日
 */
package com.taotao.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.vo.ItemCatData;
import com.taotao.common.vo.ItemCatResult;
import com.taotao.manage.mapper.ItemCatMapper;
import com.taotao.manage.pojo.ItemCat;

/**
 * @author fufu
 *
 */
@Service
public class ItemCatService  extends BaseService<ItemCat>{

	/**
	 * @author wangchuanfu
	 * 2017年4月16日
	 * @param l 
	 */
	@Autowired
	private  ItemCatMapper itemCatMapper;
	public List<ItemCat> queryListById(Long id) {
		
		
	
		
		 ItemCat  itemCat=new ItemCat();
		itemCat.setParentId(id);
		return super.queryListByWhere(itemCat);
		
		
		
	}
	public ItemCatResult queryItemCatWebAll() {

        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> cats = super.queryAll();

        // 转为map存储，key为父节点ID，value为数据集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();
        for (ItemCat itemCat : cats) {
            if (!itemCatMap.containsKey(itemCat.getParentId())) {
                itemCatMap.put(itemCat.getParentId(), new ArrayList<ItemCat>());
            }
            itemCatMap.get(itemCat.getParentId()).add(itemCat);
        }

        // 封装一级对象
        
        //获取所有的一级对象,遍历一级数据,
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData = new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");
            result.getItemCats().add(itemCatData);
            if (!itemCat.getIsParent()) {
                continue;
            }
            // 封装二级对象
            
            //获取所有的二级数据
            List<ItemCat> itemCatList2 = itemCatMap.get(itemCat.getId());
            List<ItemCatData> itemCatData2 = new ArrayList<ItemCatData>();
            //把遍历出的所有二级数据放到一级的一个实体中
            itemCatData.setItems(itemCatData2);
            for (ItemCat itemCat2 : itemCatList2) {
            	//二级中的一个实体对象
                ItemCatData id2 = new ItemCatData();
                id2.setName(itemCat2.getName());
                id2.setUrl("/products/" + itemCat2.getId() + ".html");
                //将二级中name , url 封装到二级集合中的一个实体中
                itemCatData2.add(id2);
                if (itemCat2.getIsParent()) {
                    // 封装三级对象
                    List<ItemCat> itemCatList3 = itemCatMap.get(itemCat2.getId());//所有的三级数据
                    List<String> itemCatData3 = new ArrayList<String>();
                    //将所有的三级数据封装在二级的一个对象中
                    id2.setItems(itemCatData3);
                    for (ItemCat itemCat3 : itemCatList3) {
                    	//将三级中Id,Name数据封装在三级的实体中
                    	
                        itemCatData3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size() >= 14) {
                break;
            }
        }
        return result;
    }

}
