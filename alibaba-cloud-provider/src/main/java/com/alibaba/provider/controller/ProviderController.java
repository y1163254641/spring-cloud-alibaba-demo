package com.alibaba.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
@RefreshScope
public class ProviderController {

  @Value("${spring.application.name}")
  private String name;

  @Value("${server.port}")
  private String port;

  @Autowired
  private ConfigurableApplicationContext ctx;

  @RequestMapping("")
  public String getNameList() {
    return name + "生产者" + port;
  }

  @RequestMapping("/test/{id}")
  public String getMsg(@PathVariable Integer id) {
    return "msg "+ id + " >> " + ctx.getEnvironment().getProperty("spring.application.name");
  }
}
