package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.VisitRecord;
import com.soft.service.VisitRecordService;
import com.soft.mapper.VisitRecordMapper;
import org.springframework.stereotype.Service;

/**
* @description 针对表【t_visit_record】的数据库操作Service实现
*/
@Service
public class VisitRecordServiceImpl extends ServiceImpl<VisitRecordMapper, VisitRecord>
        implements VisitRecordService {

}
