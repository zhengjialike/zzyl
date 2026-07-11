package com.soft.dto.Customer;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerResponseDto {
    private Integer id;             // 客户ID
    private String nickname;        // 客户昵称
    private String phone;           // 客户手机号
    private String isSigned;        // 是否签约（是/否）
    private Integer orderCount;     // 服务下单次数
    private String elderlyNames;    // 绑定老人姓名（多个用逗号分隔）
    private LocalDateTime createTime; // 首次登录时间
}