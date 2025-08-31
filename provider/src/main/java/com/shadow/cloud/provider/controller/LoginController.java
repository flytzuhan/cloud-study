package com.shadow.cloud.provider.controller;

import com.shadow.cloud.common.api.CommonResult;
import com.shadow.cloud.provider.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/detail")
    public CommonResult<String> detail(String phone) {
        String detail = loginService.detail(phone);
        return CommonResult.success(detail);
    }
}
