package com.alibaba.provider.service.slave.impl;

import com.alibaba.provider.mapper.slave.SlaveTestMapper;
import com.alibaba.provider.model.TestModel;
import com.alibaba.provider.service.slave.SlaveTestService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@DS("slave")
public class SlaveTestServiceImpl
    extends ServiceImpl<SlaveTestMapper, TestModel>
    implements SlaveTestService {

}
