package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.entity.InoutRecord;
import com.soft.mapper.InoutRecordMapper;
import com.soft.service.InoutRecordService;
import org.springframework.stereotype.Service;

@Service
public class InoutRecordServiceImpl extends ServiceImpl<InoutRecordMapper, InoutRecord>
        implements InoutRecordService {
}
