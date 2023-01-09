package com.xxf.apkmanagement.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "查询公司所有用户")
public class SearchAppByCompanyIdForm {
/*
    @NotNull(message = "公司ID不能为空")
    @Min(value = 1, message = "companyId不能小于1")
    @Schema(description = "公司ID")
    private Integer companyId;
*/

    @Schema(description = "模糊查询")
    private String keyword;


    @NotNull(message = "page不能为空")
    @Min(value = 1, message = "page不能小于1")
    @Schema(description = "页数")
    private Integer page;

    @NotNull(message = "length不能为空")
    @Range(min = 10, max = 50, message = "length必须在10~50之间")
    @Schema(description = "每页记录数")
    private Integer length;
}
