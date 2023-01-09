package com.xxf.apkmanagement.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Schema(description = "管理员注册")
public class RegisterForm {
    @NotBlank(message = "username不能为空")
//    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "username内容不正确")
    @Schema(description = "用户名")
    private String userName;

    @NotBlank(message = "password不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "password格式不正确")
    @Schema(description = "密码")
    private String userPassword;

    @Schema(description = "公司id")
    private long companyId;

    @NotBlank(message = "companyName不能为空")
    @Schema(description = "公司名称")
    private String companyName;
    @AssertTrue(message = "root只能是true，可不传")
    @Schema(description = "是否是管理员")
    private boolean root;
    @NotBlank(message = "phone不能为空")
    @Pattern(regexp = "^[0-9]{11}$", message = "phone格式不正确")
    @Schema(description = "手机号")
    private String phone;
}
