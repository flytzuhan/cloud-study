package com.shadow.cloud.dubbo.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DubboResponse {

    private boolean success;

    private Object result;

    private String error;

    private String interfaceName;

    private String methodName;

    private LocalDateTime timestamp;
    
    private long executeTime;

    public static DubboResponse success(Object result) {
        DubboResponse response = new DubboResponse();
        response.setSuccess(true);
        response.setResult(result);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    public static DubboResponse error(String error) {
        DubboResponse response = new DubboResponse();
        response.setSuccess(false);
        response.setError(error);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }
}
