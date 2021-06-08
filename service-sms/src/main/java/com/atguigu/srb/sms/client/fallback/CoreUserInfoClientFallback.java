package com.atguigu.srb.sms.client.fallback;

import com.atguigu.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CoreUserInfoClientFallback implements CoreUserInfoClient {

    @Override
    public boolean checkMobile(String mobile) {
        log.error("远程调用失败，服务熔断");
        return false;
    }
}
