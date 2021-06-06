package com.atguigu.srb.sms.service.impl;

import com.atguigu.common.exception.GlobalRuntimeException;
import com.atguigu.common.result.ResultEnum;
import com.atguigu.srb.sms.service.SmsService;
import com.atguigu.srb.sms.utils.MailClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private MailClient mailClient;

    private static final String SUBJECT = "验证码";



    @Override
    public void sendEmail(String email,String code) {
        try {
            mailClient.sendMail(email,SUBJECT,"您的验证码为" + code + ",有效期为五分钟，请勿泄露" );
        } catch (Exception e) {
            log.info("验证码发送失败：" + e.getMessage());
            throw new GlobalRuntimeException(ResultEnum.CODE_ERROR);
        }
    }
}
