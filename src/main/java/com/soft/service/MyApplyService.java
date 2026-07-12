package com.soft.service;

import com.soft.dto.MyApplyPageDto;

import java.util.Map;

public interface MyApplyService {

    Map<String, Object> pageList(MyApplyPageDto dto, String applicant);
}