package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.common.PageResult;
import com.zzyl.entity.Leave;

public interface LeaveService extends IService<Leave> {
    PageResult<Leave> findPage(int pageNum, int pageSize, String leaveNo, Integer elderlyId, Integer status);
    void addLeave(Leave leave, Long userId);
    void returnBack(Long id, String actualReturnTime, String remark, Long userId);
}
