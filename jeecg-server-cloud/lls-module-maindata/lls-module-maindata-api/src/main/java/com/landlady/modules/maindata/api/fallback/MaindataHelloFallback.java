package com.landlady.modules.maindata.api.fallback;

import org.springframework.cloud.openfeign.FallbackFactory;
import com.landlady.modules.maindata.api.MaindataHelloApi;
import lombok.Setter;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JeecgBoot
 */
@Slf4j
@Component
public class MaindataHelloFallback implements FallbackFactory<MaindataHelloApi> {
    @Setter
    private Throwable cause;

    @Override
    public MaindataHelloApi create(Throwable throwable) {
        log.error("微服务接口调用失败： {}", cause);
        return null;
    }

}
