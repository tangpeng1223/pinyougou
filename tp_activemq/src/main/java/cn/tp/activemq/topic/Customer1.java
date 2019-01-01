package cn.tp.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Date:2018/12/29
 * Author 唐鹏
 * DESC: 消费者1
 */
public class Customer1 {

    public static void main(String[] args)throws  Exception {

        String brokerURL="tcp://192.168.12.168:61616";
        ActiveMQConnectionFactory factor=new ActiveMQConnectionFactory(brokerURL);

        factor.setClientID("customers-1");
        Connection connection = factor.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("mytopic");
        // 4. 从连接对象里获取session
        // 第一个参数的作用是，是否使用JTA分布式事务，设置为false不开启
        // 第二个参数是设置应答方式，如果第一个参数是true，那么第二个参数就失效了，这里设置的应答方式是自动应答
        MessageConsumer consumer = session.createDurableSubscriber(topic, "customers-1");


        //接受消息
        while(true){
            Message message=consumer.receive(2000);
            if(message instanceof  TextMessage){
                TextMessage textMessage= (TextMessage) message;
                System.out.println("消费者一接收到的消息为："+textMessage.getText());
            }
        }

    }
}
