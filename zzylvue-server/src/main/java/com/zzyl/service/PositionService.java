package com.zzyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzyl.common.PageResult;
import com.zzyl.entity.Position;

public interface PositionService extends IService<Position> {
    PageResult<Position> findPage(int pageNum, int pageSize, String positionName, String status, Long deptId);
    void addPosition(Position position);
    void updatePosition(Position position);
    void updateStatus(Long id, String status);
}
