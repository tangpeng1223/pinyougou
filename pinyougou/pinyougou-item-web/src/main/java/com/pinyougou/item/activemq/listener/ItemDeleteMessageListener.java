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
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Date:2018/12/30
 * Author
 * DESC: 监听消息生成静态页面
 */
public class ItemDeleteMessageListener extends AbstractAdaptableMessageListener {

    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;


    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        ObjectMessage objectMessage= (ObjectMessage) message;
        Long[] ids = (Long[]) objectMessage.getObject();
        if(ids!=null && ids.length>0){
            for (Long id : ids) {
                String fileName=ITEM_HTML_PATH+id+".html";
                File file=new File(fileName);
                if(file.exists()){
                    //如果文件存在则删除
                   file.delete();
                }
            }
        }
    }
}
