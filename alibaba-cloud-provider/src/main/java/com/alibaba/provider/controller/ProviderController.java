package com.alibaba.provider.controller;

import com.alibaba.provider.service.master.MasterTestService;
import com.alibaba.provider.service.slave.SlaveTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
@RefreshScope
public class ProviderController {

  @Value("${spring.application.name}")
  private String name;

  @Value("${server.port}")
  private String port;

  @Autowired
  private MasterTestService masterTestService;

  @Autowired
  private SlaveTestService slaveTestService;

  @Autowired
  private ConfigurableApplicationContext ctx;

  @RequestMapping("")
  public String getNameList() {
    return name + "生产者" + port;
  }

  @GetMapping("/test/{id}")
  public String queryTest(@PathVariable Integer id) {
    return slaveTestService.list().toString() + "------->" + id;
  }


  @DeleteMapping("/test/{id}")
  public String deleteTest(@PathVariable Integer id) {
    return masterTestService.removeById(id) + "------->" + id;
  }
}
