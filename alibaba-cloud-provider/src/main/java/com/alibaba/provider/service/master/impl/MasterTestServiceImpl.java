package com.alibaba.provider.service.master.impl;


import com.alibaba.provider.mapper.master.MasterTestMapper;

import com.alibaba.provider.model.TestModel;
import com.alibaba.provider.service.master.MasterTestService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
//@DS("master")
public class MasterTestServiceImpl
    extends ServiceImpl<MasterTestMapper, TestModel>
    implements MasterTestService {

}
