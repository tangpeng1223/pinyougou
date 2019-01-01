package cn.tp.activemq.spring.queue;

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
public class QueueMessageListener extends AbstractAdaptableMessageListener {
    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        if(message instanceof TextMessage){
           TextMessage textMessage= (TextMessage) message;
            try {
                String text = textMessage.getText();
                System.out.println("监听器接受到的消息为："+text);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
