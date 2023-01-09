package com.xxf.apkmanagement.db.model;

import lombok.Data;

@Data
public class ApkUpdateModel {
    private boolean must;//是否必须更新
    private String fileSize;//文件大小
    private String content;//更新内容
    private String vName;//版本名称
    private long vCode;//版本号
    private String url;//下载链接

}
