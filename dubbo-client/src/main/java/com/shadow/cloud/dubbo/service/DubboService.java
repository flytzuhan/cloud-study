package com.shadow.cloud.dubbo.service;

import com.shadow.cloud.dubbo.model.DubboRequest;
import com.shadow.cloud.dubbo.model.DubboResponse;

public interface DubboService {

    DubboResponse invoke(DubboRequest request);

    void clearCache();
}
