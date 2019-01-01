package cn.tp.activemq.spring.queue;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

/**
 * Date:2018/12/29
 * Author 唐鹏
 * DESC: 整合spring 的生产者
 */
public class Producer {

    public static void main(String[] args) {

        //创建容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq-queue.xml");

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        Destination destination = (Destination) context.getBean("queurDestionation");
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage=new ActiveMQTextMessage();
                textMessage.setText("这是整合了spring的生产者的消息");
                System.out.println("生产者已经发送消息");
                return textMessage;
            }
        });

    }
}
