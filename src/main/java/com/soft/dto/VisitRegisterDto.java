package com.soft.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 来访登记DTO（用于直接登记）
 */
@Data
public class VisitRegisterDto {
    @NotBlank(message = "来访类型不能为空")
    private String visitType;

    @NotBlank(message = "来访人姓名不能为空")
    @Size(max = 10, message = "来访人姓名最多10个字符")
    private String visitorName;

    @NotBlank(message = "来访人手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误，请重新输入")
    @Size(max = 11, message = "手机号最多11个字符")
    private String visitorPhone;

    @NotBlank(message = "老人姓名不能为空")
    @Size(max = 10, message = "老人姓名最多10个字符")
    private String elderName;

    @NotNull(message = "来访时间不能为空")
    private LocalDateTime visitTime;

    private String remark;
}