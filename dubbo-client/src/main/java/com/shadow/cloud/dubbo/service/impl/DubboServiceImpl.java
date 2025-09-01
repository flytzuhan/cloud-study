package com.shadow.cloud.dubbo.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.shadow.cloud.dubbo.model.DubboRequest;
import com.shadow.cloud.dubbo.model.DubboResponse;
import com.shadow.cloud.dubbo.service.DubboService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DubboServiceImpl implements DubboService {

    private final Cache<String, GenericService> serviceCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(300, TimeUnit.SECONDS)
            .build();

    @Override
    public DubboResponse invoke(DubboRequest request) {
        long startTime = System.currentTimeMillis();
        try {
            // 参数验证
            validateRequest(request);

            // 获取泛化服务引用
            GenericService genericService = getGenericService(request);

            // 转换参数
            String[] paramTypeArray = request.getParamTypes().toArray(new String[0]);
            Object[] paramValueArray = request.getParamValues().toArray();

            // 执行泛化调用
            Object result = genericService.$invoke(
                    request.getMethodName(),
                    paramTypeArray,
                    paramValueArray
            );

            // 构造响应
            DubboResponse response = DubboResponse.success(result);
            response.setInterfaceName(request.getInterfaceName());
            response.setMethodName(request.getMethodName());
            response.setExecuteTime(System.currentTimeMillis() - startTime);

            log.info("Dubbo调用成功: {}#{}, 耗时: {}ms",
                    request.getInterfaceName(), request.getMethodName(),
                    response.getExecuteTime());

            return response;

        } catch (Exception e) {
            log.error("Dubbo调用失败: {}#{}, 错误: {}",
                    request.getInterfaceName(), request.getMethodName(), e.getMessage(), e);

            DubboResponse response = DubboResponse.error(e.getMessage());
            response.setInterfaceName(request.getInterfaceName());
            response.setMethodName(request.getMethodName());
            response.setExecuteTime(System.currentTimeMillis() - startTime);

            return response;
        }
    }

    private void validateRequest(DubboRequest request) {
        if (request.getParamTypes().size() != request.getParamValues().size()) {
            throw new IllegalArgumentException("参数类型和参数值数量不匹配");
        }
    }

    private GenericService getGenericService(DubboRequest request) {
        String cacheKey = buildCacheKey(request);

        try {
            return serviceCache.get(cacheKey, () -> createGenericService(request));
        } catch (Exception e) {
            throw new RuntimeException("创建泛化服务失败: " + e.getMessage(), e);
        }
    }

    private String buildCacheKey(DubboRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getInterfaceName());
        if (StringUtils.isNotBlank(request.getVersion())) {
            sb.append(":").append(request.getVersion());
        }
        if (StringUtils.isNotBlank(request.getGroupName())) {
            sb.append(":").append(request.getGroupName());
        }
        if (StringUtils.isNotBlank(request.getRegistry())) {
            sb.append("@").append(request.getRegistry());
        }
        return sb.toString();
    }

    private GenericService createGenericService(DubboRequest request) {
        log.info("创建泛化服务: {}", request.getInterfaceName());

        // 应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("dubbo-rpc-tool-consumer");

        // 注册中心配置
        RegistryConfig registry = new RegistryConfig();
        String registryAddress = StringUtils.isNotBlank(request.getRegistry()) ?
                request.getRegistry() : "zookeeper://127.0.0.1:2181";
        registry.setAddress(registryAddress);
        registry.setTimeout(10000);

        // 引用配置
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(request.getInterfaceName());
        reference.setGeneric(true);

        // 设置版本和分组
        if (StringUtils.isNotBlank(request.getVersion())) {
            reference.setVersion(request.getVersion());
        }
        if (StringUtils.isNotBlank(request.getGroupName())) {
            reference.setGroup(request.getGroupName());
        }

        // 设置超时时间
        int timeout = request.getTimeout() != null ? request.getTimeout() : 30000;
        reference.setTimeout(timeout);
        reference.setRetries(0);
        reference.setCheck(false);

        // 获取泛化服务
        return reference.get();
    }

    public void clearCache() {
        serviceCache.invalidateAll();
        log.info("已清空服务缓存");
    }
}
