package com.xxf.apkmanagement.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "检验登陆验证码表单")
public class CheckCodeForm {
    @NotBlank(message = "uuid不能为空")
    @Schema(description = "uuid")
    private String uuid;

    @NotBlank(message = "临时授权不能为空")
    @Schema(description = "临时授权")
    private String code;

}
