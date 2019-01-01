package cn.tp.activemq.spring.topic;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Date:2018/12/29
 * Author
 * DESC:
 */
public class Customer1 {

    public static void main(String[] args) {
        //创建容器
        new ClassPathXmlApplicationContext("applicationContext-activemq-topic-customer1.xml");
    }
}
