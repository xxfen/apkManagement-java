package com.xxf.apkmanagement.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import java.sql.Timestamp;

@Data
@Schema(description = "创建链接")
public class CreateLinkForm {
    @Schema(description = "失效时间")
    @Future
    private Timestamp deadTime;
    @Schema(description = "限制下载次数")
    private int disabledCount;

    @Schema(description = "安装包id")
    @Min(value = 0)
    private int apkId;
}
