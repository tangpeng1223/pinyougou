package cn.tp.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Date:2018/12/29
 * Author 唐鹏
 * DESC: 消费者
 */
public class Customers {

    public static void main(String[] args) throws  Exception {
        String borkerURL="tcp://192.168.12.168:61616";

        ActiveMQConnectionFactory factory=new ActiveMQConnectionFactory(borkerURL);


        Connection connection = factory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);


        //从session中获取消息类型
        Queue myqueue = session.createQueue("myqueue");

        //从session中获取消息的消费者
        MessageConsumer consumer = session.createConsumer(myqueue);

        while(true){
            //设置接收消息的等待时间 为2秒
            Message message=consumer.receive(2000);
            //判断消息的类型
            if(message instanceof  TextMessage){
                TextMessage textMessage= (TextMessage) message;
                System.out.println("消费者接受到的消息为"+textMessage.getText());
            }
        }
    }
}
