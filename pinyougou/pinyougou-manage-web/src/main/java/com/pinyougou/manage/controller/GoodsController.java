package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyou.search.service.ItemSearchService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import javax.xml.soap.Text;
import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemSearchService itemSearchService;


    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQQueue itemImportQueue;

    @Autowired
    private ActiveMQQueue itemDeleteQueue;

    @Autowired
    private ActiveMQTopic itemTopic;

    @Autowired
    private ActiveMQTopic itemDeleteTopic;

    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return goodsService.findPage(page, rows);
    }

    @PostMapping("/add")
    public Result add(@RequestBody TbGoods goods) {
        try {
            goodsService.add(goods);
            return Result.Success("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("增加失败");
    }

    @GetMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findGoodsById(id);
    }

    @PostMapping("/update")
    public Result update(@RequestBody TbGoods goods) {
        try {
            goodsService.update(goods);
            return Result.Success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    /**
     * 删除商品
     * @param ids 要删除的商品的id数组
     * @return Result 处理结果
     */
    @GetMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.deleteGoodsByIds(ids);
            //itemSearchService.deleteItemListByGoodsIds(ids);
            //发送消息删除solr中的数据
            sendMsg(itemDeleteQueue,ids);
            //发送消息删除静态华页面
            sendMsg(itemDeleteTopic,ids);
            return Result.Success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 发送队列消息
     * @param destination  删除消息名
     * @param ids spu数组
     */
    private void sendMsg(Destination destination, Long[] ids) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(ids);
            }
        });

    }

    /**
     * 分页查询列表
     * @param goods 查询条件
     * @param page 页号
     * @param rows 每页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody  TbGoods goods, @RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return goodsService.search(page, rows, goods);
    }

    @GetMapping("/updateStatus")
    public Result updateStatus(Long ids[],String status){
        try {
            goodsService.updateStatus(ids,status);
            if("2".equals(status)){
                //查询修改spu对应的sku
                List<TbItem> itemList=goodsService.findItemListByGoodsIdsAndStatus(ids,"1");
                //itemSearchService.importItemList(itemList);
                jmsTemplate.send(itemImportQueue, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage textMessage=new ActiveMQTextMessage();
                        //转化格式
                        textMessage.setText(JSON.toJSONString(itemList));
                        return textMessage;
                    }
                });

                //发送消息到item_web商品详情系统
                sendMsg(itemTopic,ids);
            }
            return  Result.Success("审核成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("审核失败");
    }
}
