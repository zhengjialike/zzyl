package com.zzyl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzyl.common.PageResult;
import com.soft.pojo.Position;
import com.zzyl.mapper.PositionMapper;
import com.zzyl.service.PositionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements PositionService {

    @Override
    public PageResult<Position> findPage(int pageNum, int pageSize, String positionName, String status, Long deptId) {
        Page<Position> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Position> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(positionName)) {
            wrapper.like(Position::getPositionName, positionName);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Position::getStatus, status);
        }
        if (deptId != null) {
            wrapper.eq(Position::getDeptId, deptId);
        }
        wrapper.orderByDesc(Position::getCreateTime);
        IPage<Position> iPage = baseMapper.selectPage(page, wrapper);
        return new PageResult<>(iPage.getRecords(), iPage.getTotal(), iPage.getCurrent(), iPage.getSize());
    }

    @Override
    public void addPosition(Position position) {
        baseMapper.insert(position);
    }

    @Override
    public void updatePosition(Position position) {
        baseMapper.updateById(position);
    }

    @Override
    public void updateStatus(Long id, String status) {
        Position position = new Position();
        position.setId(id.intValue());
        position.setStatus(Integer.valueOf(status));
        baseMapper.updateById(position);
    }
}
