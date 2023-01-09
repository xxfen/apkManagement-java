package com.xxf.apkmanagement.db.pojo;


import lombok.Data;

import java.io.Serializable;
@Data
public class TbUser  implements Serializable {
  private long id;
  private long userId;
  private String userPassword;
  private String userName;
  private long companyId;
  private boolean root;
  private String phone;
  private java.sql.Timestamp creationTime;
}
