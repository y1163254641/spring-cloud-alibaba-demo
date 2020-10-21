package com.alibaba.feign.controller;

import com.alibaba.feign.feign.ProviderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController implements ProviderFeign {

  @Autowired
  private ProviderFeign providerFeign;


  @Override
  public String getMsg(Integer id) {
    return "frontend: " + providerFeign.getMsg(id);
  }
}
