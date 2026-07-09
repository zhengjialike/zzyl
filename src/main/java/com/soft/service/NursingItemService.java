package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.dto.NursingItemDto;
import com.soft.pojo.NursingItem;

import java.util.Map;

public interface NursingItemService extends IService<NursingItem> {

    /**
     * 分页条件查询护理项目
     * @param nursingItemDto 查询条件（包含分页参数和过滤条件）
     * @return 包含 total（总记录数）和 nursingItems（列表）的 Map
     */
    Map<String, Object> queryNursingItemList(NursingItemDto nursingItemDto);
}