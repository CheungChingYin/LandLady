package com.landlady.modules.maindata.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.landlady.modules.maindata.entity.MaindataHelloEntity;
import com.landlady.modules.maindata.service.IMaindataHelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "maindata示例")
@RestController
@RequestMapping("/maindata")
@Slf4j
public class MaindataHelloController {

	@Autowired
	private IMaindataHelloService jeecgHelloService;

	@ApiOperation(value = "hello", notes = "对外服务接口")
	@GetMapping(value = "/hello")
	public String sayHello() {
		log.info(" ---我被调用了--- ");
		return jeecgHelloService.hello();
	}

}
