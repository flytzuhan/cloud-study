package com.shadow.cloud.dubbo.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
public class DubboRequest {

    @NotEmpty(message = "注册中心不能为空")
    private String registry;

    @NotEmpty(message = "接口名称不能为空")
    private String interfaceName;

    @NotEmpty(message = "方法名称不能为空")
    private String methodName;

    @NotEmpty(message = "参数类型不能为空")
    private List<String> paramTypes;

    @NotEmpty(message = "参数值不能为空")
    private List<Object> paramValues;

    @NotEmpty(message = "版本号不能为空")
    private String version;

    @NotEmpty(message = "组名称不能为空")
    private String groupName;

    private Integer timeout;

    private Map<String, String> attachments;
}
