package com.alibaba.provider.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 测试
 *
 * @author yanggongming
 */
@TableName("test")
@Data
public class TestModel {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String name;


}
