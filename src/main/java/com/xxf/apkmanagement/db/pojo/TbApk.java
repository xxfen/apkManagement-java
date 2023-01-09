package com.xxf.apkmanagement.db.pojo;


import lombok.Data;

import java.io.Serializable;

@Data
public class TbApk  implements Serializable {

  private long id;
  private String appPackage;
  private String vName;
  private boolean forcedUpdating;
  private long vCode;
  private String apkPath;
  private String content;
  private String fileSize;
  private java.sql.Timestamp creationTime;

}
