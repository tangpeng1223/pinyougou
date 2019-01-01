package cn.tp.activemq.spring.topic;

import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Date:2018/12/29
 * Author
 * DESC:
 */
public class CustomerMessageListener2 extends AbstractAdaptableMessageListener {
    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        if(message instanceof TextMessage){
            try {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("消费者二监听器接收到的消息为："+textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
