package com.xxf.apkmanagement.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import javax.validation.constraints.Min;

@Data
@Schema(description = "查询安装包链接")
public class SearchLinkByApkIdFrom {
    @Min(value = 0)
    @Schema(description = "安装包id")
    private int apkId;
}
