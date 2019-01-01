package cn.tp.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.xml.soap.Text;
import java.util.function.Consumer;

/**
 * Date:2018/12/29
 * Author 唐鹏
 * DESC: 消费者2
 */
public class Customer2 {

    public static void main(String[] args) throws Exception{

        String  brokerURL ="tcp://192.168.12.168:61616";
        ActiveMQConnectionFactory factory=new ActiveMQConnectionFactory(brokerURL);

        factory.setClientID("customers-2");

        //使用工厂创建连接池
        Connection connection = factory.createConnection();
        //开启连接
        connection.start();

        //获取session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //获取sesson中的类型模板
        Topic mytopic = session.createTopic("mytopic");
        MessageConsumer consumers = session.createDurableSubscriber(mytopic, "customers-2");

        //设置监听器 监听
        consumers.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if(message instanceof  TextMessage){
                        TextMessage textMessage=(TextMessage)message;
                        System.out.println("消费者2接受的消息为："+textMessage.getText());
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //让主线程休眠 子线程接受消息
        Thread.sleep(20000);

        //释放资源
        session.close();
        connection.close();
        consumers.close();
    }
}
