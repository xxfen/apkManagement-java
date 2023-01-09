package com.xxf.apkmanagement.db.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;


@Data
public class AppApkModel {
    private String appPackage;
    private String appName;
    private String apkPath;
    private String companyName;
    private String content;
    private long id;
    private long apkId;
    private long companyId;
    private long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp creationTime;
    private String vName;
    private String fileSize;
    private String url;
    private boolean forcedUpdating;
    private long vCode;

}
