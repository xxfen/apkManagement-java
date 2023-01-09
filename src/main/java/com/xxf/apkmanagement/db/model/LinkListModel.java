package com.xxf.apkmanagement.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class LinkListModel {
    private long id;
    private long userId;
    private boolean disabled;
    private long apkId;
    private String link;
    private String appName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private java.sql.Timestamp deadTime;
    private long count;
    private long disabledCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private java.sql.Timestamp creationTime;
    private String vName;
    private long vCode;
}
