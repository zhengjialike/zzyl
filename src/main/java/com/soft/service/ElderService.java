package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.Elder;

public interface ElderService extends IService<Elder> {
    Elder queryByIdCard(String idCard);
}