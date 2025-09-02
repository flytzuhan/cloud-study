package com.shadow.cloud.provider.dubbo;

import com.shadow.cloud.dubbo.api.UserInfoDubboApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@DubboService(version = "1.2.0", timeout = 5000, group = "provider-group")
@Component
public class UserInfoDubboApiImpl implements UserInfoDubboApi {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public Integer getUserLoginCount(String userId) {
        log.info("getUserLoginCount: {}", userId);
        return (Integer) redissonClient.getBucket(getCacheKey(userId)).get();
    }

    private static String getCacheKey(String userId) {
        return "user_login_count_" + userId;
    }
}
