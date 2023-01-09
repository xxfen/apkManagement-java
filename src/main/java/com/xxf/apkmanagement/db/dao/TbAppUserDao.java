package com.xxf.apkmanagement.db.dao;

import com.xxf.apkmanagement.db.pojo.TbAppUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TbAppUserDao {
    int insertAppUser(HashMap hashMap);

    TbAppUser searchAppUserByUserName(HashMap map);
}
