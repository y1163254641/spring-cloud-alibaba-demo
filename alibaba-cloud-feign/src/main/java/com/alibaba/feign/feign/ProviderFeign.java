package com.alibaba.feign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "alibaba-cloud-provider")
public interface ProviderFeign {

  @RequestMapping("/provider/test/{id}")
  public String getMsg(@PathVariable Integer id);
}
