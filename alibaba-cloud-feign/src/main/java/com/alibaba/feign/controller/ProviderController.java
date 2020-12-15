package com.alibaba.feign.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.feign.feign.ProviderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ProviderController implements ProviderFeign {

  @Autowired
  private ProviderFeign providerFeign;


  @Override
  @SentinelResource(value = "/provider", blockHandler = "handleBlock")
  public String queryTest(Integer id) {
    return "query-provider: " + providerFeign.queryTest(id);
  }

  public String handleBlock(Integer id, BlockException e){
    return  "id:" + id + "限流" + e.getMessage();
  }

  @Override
  @SentinelResource(value = "/test")
  public String queryTest2(Integer id) {
    return "query-test: " + providerFeign.queryTest2(id);
  }

  @Override
  public String deleteTest(Integer id) {
    return "delete: " + providerFeign.deleteTest(id);
  }
}
