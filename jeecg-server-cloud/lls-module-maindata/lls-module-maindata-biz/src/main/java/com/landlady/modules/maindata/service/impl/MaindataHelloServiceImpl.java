package com.landlady.modules.maindata.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.landlady.modules.maindata.entity.MaindataHelloEntity;
import com.landlady.modules.maindata.mapper.MaindataHelloMapper;
import com.landlady.modules.maindata.service.IMaindataHelloService;
import org.springframework.stereotype.Service;

/**
 * 测试Service
 */
@Service
public class MaindataHelloServiceImpl extends ServiceImpl<MaindataHelloMapper, MaindataHelloEntity> implements IMaindataHelloService {

    @Override
    public String hello() {
        return "hello ，我是 maindata 微服务节点!";
    }
}
