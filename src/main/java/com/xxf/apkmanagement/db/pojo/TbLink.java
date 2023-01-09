package com.xxf.apkmanagement.db.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;

@Data
public class TbLink  implements Serializable {

  private long id;
  private long userId;
  private boolean disabled;
  private int apkId;
  private String link;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private java.sql.Timestamp deadTime;
  private long count;
  private long disabledCount;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private java.sql.Timestamp creationTime;




}
