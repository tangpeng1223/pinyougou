package cn.tp.controller;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Date:2018/12/31
 * Author
 * DESC:
 */
@RequestMapping("/mq")
@RestController
public class MqController {

    @Autowired
    private JmsMessagingTemplate jmsMessageTemplate;



    @GetMapping("/sendMsg")
    public String sendMsg(){
        Map<String ,Object> map=new HashMap<>();
        map.put("name","tangpeng");
        map.put("like","lol");

        //参数一为发送队列的名称 参数二为 数据
        jmsMessageTemplate.convertAndSend("spring.boot.map.queue",map);
        return "发送消息成功";
    }

    /**
     * 发送短信
     * @return
     */
    /*@GetMapping("/sendSmsMsg")
    public String sendSmsMsg(){
        //String phoneNumbers,String signName,String templateCode,String templateParam
        //创建map集合存储数据
        Map<String ,String> map=new HashMap<>();
        map.put("phoneNumbers","13670042491");
        map.put("signName","黑马");
        map.put("templateCode","SMS_125018593");
        map.put("templateParam","{\"code\":\"123456\"}");
        jmsMessageTemplate.convertAndSend("tp_sms_queue",map);
        return  "发送短信成功";
    }
*/
    /**
     * 发送短信
     * @return
     */
    @GetMapping("/sendSmsMsg")
    public String sendSmsMsg(){
        //String phoneNumbers,String signName,String templateCode,String templateParam
        //创建map集合存储数据
        Map<String ,String> map=new HashMap<>();
        map.put("phoneNumbers","13670042491");
        map.put("signName","黑马");
        map.put("templateCode","SMS_125018593");
        map.put("templateParam","{\"code\":\"123456\"}");
        jmsMessageTemplate.convertAndSend("sms_queue",map);
        return  "发送短信成功";
    }
}
