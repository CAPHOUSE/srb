package com.atguigu.srb.sms;

import com.atguigu.srb.sms.utils.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test1 {

    @Autowired
    private MailClient mailClient;

    @Test
    public void test(){
        mailClient.sendMail("1219078112@qq.com","验证码","1234");
    }
}
