package com.zzyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    @TableField(exist = false)
    private String type;
    private Long receiverId;
    private Integer isRead;
    private String msgType;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer delFlag;
}
