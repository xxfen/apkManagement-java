package com.xxf.apkmanagement.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "查询公司所有用户")
public class SearchUsersByCompanyIdForm {
    @NotNull(message = "公司ID不能为空")
    @Min(value = 1, message = "companyId不能小于1")
    @Schema(description = "公司ID")
    private Integer companyId;
}
