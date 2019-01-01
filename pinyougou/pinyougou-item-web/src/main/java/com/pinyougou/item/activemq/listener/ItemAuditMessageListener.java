package com.pinyougou.item.activemq.listener;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Date:2018/12/30
 * Author
 * DESC: 监听消息生成静态页面
 */
public class ItemAuditMessageListener extends AbstractAdaptableMessageListener {


    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;


    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemCatService itemCatService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        ObjectMessage objectMessage= (ObjectMessage) message;
        Long[] ids = (Long[]) objectMessage.getObject();
        if(ids!=null && ids.length>0){
            for (Long id : ids) {
                genHtml(id);
            }
        }
    }

    /**
     * 创建静态文件
     * @param id
     */
    private void genHtml(Long id) {

        try {
            //创建核心对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");

            Map<String ,Object> dateModel=new HashMap<>();

            //调用业务层查询数据
            Goods goods = goodsService.findItemByGoodsIdAndStatus(id, "1");
            dateModel.put("goods",goods.getGoods());
            //设置基本信息描述
            dateModel.put("goodsDesc",goods.getGoodsDesc());

            //设置sku集合
            dateModel.put("itemList",goods.getItemList());
            //设置一级分类名
            TbItemCat itemCat1 = itemCatService.findOne(goods.getGoods().getCategory1Id());
            dateModel.put("itemCat1",itemCat1.getName());

            //设置一级分类名
            TbItemCat itemCat2 = itemCatService.findOne(goods.getGoods().getCategory2Id());
            dateModel.put("itemCat2",itemCat2.getName());
            //设置一级分类名
            TbItemCat itemCat3 = itemCatService.findOne(goods.getGoods().getCategory3Id());
            dateModel.put("itemCat3",itemCat3.getName());

            //创建输出对象
            String fileName=ITEM_HTML_PATH+id+".html";
            FileWriter fileWriter=new FileWriter(fileName);
            //渲染页面
            template.process(dateModel,fileWriter);
            fileWriter.close();
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
