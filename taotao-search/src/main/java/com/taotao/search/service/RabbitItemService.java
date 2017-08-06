package com.taotao.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taotao.common.spring.exetend.PropertyConfig;
import com.taotao.common.vo.TaotaoResult;
import com.taotao.search.pojo.Item;

@Component
public class RabbitItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitItemService.class);

    @Autowired
    private SearchService searchService;

    @Autowired
    private ApiService apiService;

    @PropertyConfig
    private String MANAGE_TAOTAO;

    public void saveOrUpdateItem(Long itemId) {
        LOGGER.info("接受到消息，内容为: {}", itemId);
        Item item = this.getItemFromApi(itemId);
        if (null != item) {
            this.searchService.update(item);
        }
    }

    private Item getItemFromApi(Long itemId) {
        try {
            String url = MANAGE_TAOTAO + "/item/query/" + itemId;
            String jsonData = this.apiService.doGet(url);
            TaotaoResult taotaoResult = TaotaoResult.formatToPojo(jsonData, Item.class);
            return (Item) taotaoResult.getData();
        } catch (Exception e) {
            LOGGER.error("更新Solr数据出错! itemId = " + itemId, e);
        }
        return null;
    }

}
