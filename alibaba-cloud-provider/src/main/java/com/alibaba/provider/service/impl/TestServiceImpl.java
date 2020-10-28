package com.alibaba.provider.service.impl;

import com.alibaba.provider.dao.mapper.TestMapper;
import com.alibaba.provider.model.TestModel;
import com.alibaba.provider.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl
    extends ServiceImpl<TestMapper, TestModel>
    implements TestService {

}
