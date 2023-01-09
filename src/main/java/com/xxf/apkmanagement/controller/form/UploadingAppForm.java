package com.xxf.apkmanagement.controller.form;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;


@Data
@Schema(description = "上传应用")
public class UploadingAppForm {

    @Schema(description = "是否强制更新")
    private boolean forcedUpdating;

    @Schema(description = "描述内容")
    @NotBlank(message = "描述内容不能为空")
    private String content;

    @Schema(description = "应用id,发布新版本要传")
    private int appId;

}
