package com.xxf.apkmanagement.db.model;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class UserListModel {
   private int id;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Timestamp creationTime;
}
