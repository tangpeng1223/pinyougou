package cn.tp.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

/**
 * Date:2018/12/29
 * Author 唐鹏
 * DESC: 订阅预发布中的生产者
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        String brokerURL="tcp://192.168.12.168:61616";
        ActiveMQConnectionFactory factory=new ActiveMQConnectionFactory(brokerURL);

        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);


        Topic mytopic = session.createTopic("mytopic");


        //获取生产者
        MessageProducer producer = session.createProducer(mytopic);

        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //设置发送消息的类型
        TextMessage message = new ActiveMQTextMessage();
        message.setText("这是订阅月发布中生产者发出的消息  topic");

        //发送消息
        producer.send(message);
        //关闭资源
        session.close();
        connection.close();
        producer.close();
    }
}
