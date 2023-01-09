package com.xxf.apkmanagement.db.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class TbAppUser {
    private long id;
    private String appPackage;
    private String appUserName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private java.sql.Timestamp creationTime;
}
