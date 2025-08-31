package com.shadow.cloud.provider.service.impl;

import com.shadow.cloud.provider.service.LoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public String detail(String phone) {
        String msg = "获取登录日志信息成功";
        // 使用异步的方式去发送消息到
        return msg;
    }
}
