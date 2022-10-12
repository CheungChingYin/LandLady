package com.landlady.modules.maindata.api;
import com.landlady.modules.maindata.api.fallback.MaindataHelloFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jeecg-maindata", fallbackFactory = MaindataHelloFallback.class)
public interface MaindataHelloApi {

    /**
     * maindata hello 微服务接口
     * @param
     * @return
     */
    @GetMapping(value = "/maindata/hello")
    String callHello();
}
