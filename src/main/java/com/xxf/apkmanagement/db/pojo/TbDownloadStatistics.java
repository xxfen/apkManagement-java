package com.xxf.apkmanagement.db.pojo;

import java.io.Serializable;

public class TbDownloadStatistics  implements Serializable {

  private long id;
  private long appUserId;
  private long apkId;
  private long count;
  private java.sql.Timestamp updateTime;
  private java.sql.Timestamp creationTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(long appUserId) {
    this.appUserId = appUserId;
  }


  public long getApkId() {
    return apkId;
  }

  public void setApkId(long apkId) {
    this.apkId = apkId;
  }


  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }


  public java.sql.Timestamp getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(java.sql.Timestamp creationTime) {
    this.creationTime = creationTime;
  }

}
