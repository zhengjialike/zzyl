package com.soft.dto.Customer;

import lombok.Data;

@Data
public class CustomerQueryDto {
    private String nickname;        // 客户昵称（模糊查询）
    private String phone;           // 客户手机号（精确查询）
    private Integer pageNum = 1;    // 当前页码，默认为1
    private Integer pageSize = 10;  // 每页大小，默认为10
}