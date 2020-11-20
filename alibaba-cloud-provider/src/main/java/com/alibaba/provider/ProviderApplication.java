package com.alibaba.provider;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 生产者启动类
 *
 * @author yanggongming
 */
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@EnableDiscoveryClient
@MapperScan("com.alibaba.**.mapper")
//@ComponentScan(basePackages = {"com.alibaba.provider"})
@EnableScheduling
public class ProviderApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProviderApplication.class, args);
  }

}
