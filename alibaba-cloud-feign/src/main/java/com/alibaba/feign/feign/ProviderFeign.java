package com.alibaba.feign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "alibaba-cloud-provider")
public interface ProviderFeign {

  @GetMapping("/provider/test/{id}")
  public String queryTest(@PathVariable Integer id);

  @DeleteMapping("/provider/test/{id}")
  String deleteTest(@PathVariable Integer id);
}
