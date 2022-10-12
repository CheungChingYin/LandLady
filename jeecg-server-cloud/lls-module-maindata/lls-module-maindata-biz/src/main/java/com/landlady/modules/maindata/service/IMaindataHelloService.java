package com.landlady.modules.maindata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.landlady.modules.maindata.entity.MaindataHelloEntity;

/**
 * 测试接口
 */
public interface IMaindataHelloService extends IService<MaindataHelloEntity> {

    String hello();

}
