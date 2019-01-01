package cn.tp.listener;


import cn.tp.utils.SmsUtils;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Date:2019/1/1
 * Author
 * DESC:
 */
@Component
public class MessageListener {

    @Autowired
    private SmsUtils smsUtils;

    @JmsListener(destination = "tp_sms_queue")
    public void  getMessage(Map<String ,String> smsMap) {
        try {
            System.out.println("发送的数据为："+smsMap);
            SendSmsResponse response = smsUtils.sendSms(smsMap.get("phoneNumbers"),
                    smsMap.get("signName"), smsMap.get("templateCode"), smsMap.get("templateParam"));
            System.out.println("短信接口返回的数据----------------");
            System.out.println("Code=" + response.getCode());
            System.out.println("Message=" + response.getMessage());
            System.out.println("RequestId=" + response.getRequestId());
            System.out.println("BizId=" + response.getBizId());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
