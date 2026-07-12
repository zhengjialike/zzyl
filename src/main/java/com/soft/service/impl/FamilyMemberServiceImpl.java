package com.soft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soft.mapper.FamilyMemberMapper;
import com.soft.pojo.FamilyMember;
import com.soft.service.FamilyMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FamilyMemberServiceImpl extends ServiceImpl<FamilyMemberMapper, FamilyMember> implements FamilyMemberService {

    @Override
    public List<FamilyMember> queryByCheckInId(Integer checkInId) {
        QueryWrapper<FamilyMember> wrapper = new QueryWrapper<>();
        wrapper.eq("check_in_id", checkInId);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void saveByCheckInId(Integer checkInId, List<FamilyMember> list) {
        QueryWrapper<FamilyMember> wrapper = new QueryWrapper<>();
        wrapper.eq("check_in_id", checkInId);
        this.baseMapper.delete(wrapper);
        if (list != null) {
            list.forEach(m -> {
                m.setId(null);
                m.setCheckInId(checkInId);
                this.baseMapper.insert(m);
            });
        }
    }
}