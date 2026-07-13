package com.zzyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("t_leave")
public class Leave {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String leaveNo;
    private Integer elderlyId;
    private String caregiverLevel;
    private String caregiver;
    private String leaveReason;
    private String companionType;
    private String companionName;
    private String companionPhone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime leaveStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expectedReturnTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime actualReturnTime;
    private Double leaveDays;
    private Double actualLeaveDays;
    private Integer status;
    private Integer applicantId;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
