package com.pinyougou.search.activemq.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyou.search.service.ItemSearchService;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/30
 * Author 唐鹏
 * DESC: 监听商品审核通过后需要通过activemq导入数据到solr中
 */
public class ItemImportMessageListener extends AbstractAdaptableMessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        List<TbItem> itemList = JSON.parseArray(textMessage.getText(), TbItem.class);
        for (TbItem tbItem : itemList) {
            Map map = JSON.parseObject(tbItem.getSpec(),Map.class);
            tbItem.setSpecMap(map);
        }
        itemSearchService.importItemList(itemList);
    }
}
