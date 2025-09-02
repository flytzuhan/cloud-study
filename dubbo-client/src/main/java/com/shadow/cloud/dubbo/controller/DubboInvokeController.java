package com.shadow.cloud.dubbo.controller;

import com.shadow.cloud.dubbo.model.DubboRequest;
import com.shadow.cloud.dubbo.model.DubboResponse;
import com.shadow.cloud.dubbo.service.DubboService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/dubbo")
public class DubboInvokeController {

    @Autowired
    private DubboService dubboService;

    @RequestMapping(value = "/invoke", method = RequestMethod.POST)
    public ResponseEntity<DubboResponse> invoke(@Validated @RequestBody DubboRequest request)
    {
        log.info("收到Dubbo调用请求: {}#{}", request.getInterfaceName(), request.getMethodName());
        try {
            DubboResponse response = dubboService.invoke(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Dubbo调用异常", e);
            return ResponseEntity.ok(DubboResponse.error("系统异常: " + e.getMessage()));
        }
    }

    @PostMapping("/dubbo/clear-cache")
    public ResponseEntity<String> clearCache() {
        dubboService.clearCache();
        return ResponseEntity.ok("缓存已清空");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
