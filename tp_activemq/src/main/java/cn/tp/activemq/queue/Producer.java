package cn.tp.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

/**
 * Date:2018/12/29
 * Author 唐鹏
 * DESC: mq中的生产者
 */
public class Producer {

    public static void main(String[] args)throws Exception {

        //使用tcp协议
        String brokerURL="tcp://192.168.12.168:61616";
        ActiveMQConnectionFactory factory=new ActiveMQConnectionFactory(brokerURL);

        //使用连接池工厂创建连接
        Connection connection=factory.createConnection();
        //使用连接对象开启连接
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue=session.createQueue("myqueue");

        //获取消息生产者
        MessageProducer producer = session.createProducer(queue);

        //创建消息体 使用textMessage
        TextMessage textMessage=new ActiveMQTextMessage();
        textMessage.setText("你好啊 activeMq");

        //使用消息生产者发送消息
        producer.send(textMessage);
        //释放资源
        producer.close();
        session.close();
        connection.close();
    }
}
