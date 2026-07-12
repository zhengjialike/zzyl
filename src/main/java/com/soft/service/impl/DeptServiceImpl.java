package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.pojo.Dept;
import com.soft.service.DeptService;
import com.soft.mapper.DeptMapper;
import org.springframework.stereotype.Service;

/**
* @author 12
* @description 针对表【t_dept】的数据库操作Service实现
* @createDate 2026-07-12 18:55:18
*/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept>
    implements DeptService{

}




