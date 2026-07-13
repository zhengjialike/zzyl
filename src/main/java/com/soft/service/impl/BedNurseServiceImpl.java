package com.soft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.BedNurseMapper;
import com.soft.pojo.BedNurse;
import com.soft.service.BedNurseService;
import org.springframework.stereotype.Service;

@Service
public class BedNurseServiceImpl extends ServiceImpl<BedNurseMapper, BedNurse> implements BedNurseService {
}
