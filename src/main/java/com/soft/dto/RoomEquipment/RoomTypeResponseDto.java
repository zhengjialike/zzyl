package com.soft.dto.RoomEquipment;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RoomTypeResponseDto {
    private Integer id;                // 房型ID
    private String typeName;           // 房型名称
    private BigDecimal price;          // 床位费用
    private String image;              // 房型图片URL
    private String description;        // 房型介绍
    private Integer status;            // 状态
    private String createUser;         // 创建人
    private LocalDateTime createTime;  // 创建时间
}