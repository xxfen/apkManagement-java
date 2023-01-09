package com.xxf.apkmanagement.db.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;


@Data
public class ApkListModel {
    private String appPackage;
    private String appName;
    private long id;
    private long userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp creationTime;
    private String vName;
    private String fileSize;
    private boolean forcedUpdating;
    private long vCode;
    private String apkPath;
    private String content;


}
