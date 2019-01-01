package cn.tp.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Date:2018/12/31
 * Author
 * DESC:
 */
@Component
public class GetMessageListener  {


    @JmsListener(destination ="spring.boot.map.queue")
    public void getMessage(Map<String,Object> map){
        String name = (String) map.get("name");
        String like = (String) map.get("like");
        System.out.println("姓名："+name +"爱好："+like);

    }
}
