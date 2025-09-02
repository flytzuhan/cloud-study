package com.shadow.cloud.provider.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RedissonConfig {

    @Value("${spring.redis.cluster.nodes:127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002}")
    private List<String> clusterNodes;

    @Bean
    public RedissonClient  redissonClient() {
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        for (String node : clusterNodes) {
            clusterServersConfig.addNodeAddress("redis://" + node);
        }

        clusterServersConfig.setConnectTimeout(2000)
                .setTimeout(2000)
                .setRetryAttempts(3)
                .setRetryInterval(1000);

        return Redisson.create(config);
    }
}
