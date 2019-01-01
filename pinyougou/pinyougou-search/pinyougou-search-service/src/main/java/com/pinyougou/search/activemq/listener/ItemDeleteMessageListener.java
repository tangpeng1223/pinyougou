package com.pinyougou.search.activemq.listener;

import com.alibaba.fastjson.JSON;
import com.pinyou.search.service.ItemSearchService;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Date:2018/12/30
 * Author 唐鹏
 * DESC: 监听商品审核通过后需要通过activemq发送队列消息删除solr中对应的数据
 */
public class ItemDeleteMessageListener extends AbstractAdaptableMessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        ObjectMessage  objectMessage =(ObjectMessage) message;
        //接受数据
        Long[]  ids = (Long[]) objectMessage.getObject();
        itemSearchService.deleteItemListByGoodsIds(ids);
        System.out.println("删除solr中的数据成功");
    }
}
