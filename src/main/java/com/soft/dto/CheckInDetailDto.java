package com.soft.dto;

import com.soft.pojo.CheckIn;
import com.soft.pojo.FamilyMember;
import lombok.Data;

import java.util.List;

@Data
public class CheckInDetailDto extends CheckIn {
    private List<FamilyMember> familyMembers;
}