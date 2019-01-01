package cn.tp.activemq.spring.topic;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;


/**
 * Date:2018/12/29
 * Author
 * DESC:
 */
public class Producer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq-topic-producer.xml");

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        //获取模式
       Destination destination = (Destination) context.getBean("topicDestination");

        jmsTemplate.send(destination, new MessageCreator() {
          @Override
          public Message createMessage(Session session) throws JMSException {
              TextMessage textMessage=new ActiveMQTextMessage();
              textMessage.setText("这是订阅月发布的生产者发送的消息：topic");

              System.out.println("生产者发布消息成功");
              return textMessage;
          }
      });

    }
}
