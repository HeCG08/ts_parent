package cn.rumoss.ts.sms.listener;

import cn.rumoss.ts.sms.util.SmsUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 短信监听类
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Value("${aliyun.sms.tempCode}")
    private String tempCode;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Autowired
    private SmsUtil smsUtil;

    @RabbitHandler
    public void sms(Map<String,String> map){

        String mobile = map.get("mobile");
        String code = map.get("code");
        System.out.println("手机号： " + mobile);
        System.out.println("验证码： " + code);
        try {
            SendSmsResponse sendSmsResponse = smsUtil.sendSms(mobile, tempCode, signName, code);

            if(sendSmsResponse.getCode().equals("OK")){
                System.out.println("短信发送成功");
            }else{
                System.out.println("短信发送失败："+sendSmsResponse.getCode());
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送短信-打印测试
     * @param map
     */
    public void smsTest(Map<String,String> map){
        String mobile = map.get("mobile");
        String code = map.get("code");
        System.out.println("手机号： " + mobile);
        System.out.println("验证码： " + code);
    }
}
