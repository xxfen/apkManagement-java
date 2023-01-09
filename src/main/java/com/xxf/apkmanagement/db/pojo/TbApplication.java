package com.xxf.apkmanagement.db.pojo;


import lombok.Data;

import java.io.Serializable;

@Data
public class TbApplication implements Serializable {
    private String appPackage;
    private String appName;
    private long companyId;
    private long userId;
    private java.sql.Timestamp creationTime;


}
