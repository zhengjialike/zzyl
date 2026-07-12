package com.soft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soft.pojo.FamilyMember;

import java.util.List;

public interface FamilyMemberService extends IService<FamilyMember> {
    List<FamilyMember> queryByCheckInId(Integer checkInId);
    void saveByCheckInId(Integer checkInId, List<FamilyMember> list);
}