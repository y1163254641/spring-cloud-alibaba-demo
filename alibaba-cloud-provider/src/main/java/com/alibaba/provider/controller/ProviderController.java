package com.alibaba.provider.controller;

import com.alibaba.provider.model.TestModel;
import com.alibaba.provider.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/provider")
@RefreshScope
public class ProviderController {

  @Value("${spring.application.name}")
  private String name;

  @Value("${server.port}")
  private String port;

  @Autowired
  private TestService testService;

  @Autowired
  private ConfigurableApplicationContext ctx;

  @RequestMapping("")
  public String getNameList() {
    return name + "生产者" + port;
  }

  @RequestMapping("/test/{id}")
  public String getMsg(@PathVariable Integer id) {
    return testService.list().toString() + "------->" + id;
  }
}
