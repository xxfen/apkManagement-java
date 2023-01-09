package com.xxf.apkmanagement.db.dao;

import com.xxf.apkmanagement.db.model.UserListModel;
import com.xxf.apkmanagement.db.pojo.TbUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;


@Mapper
public interface TbUserDao {
     ArrayList<UserListModel> searchAllUser(int companyId);

     boolean getRole(int userId);

     Integer login(HashMap param);

     int insert(TbUser tbUser);

    HashMap searchUserById(int userId);

    TbUser searchUserByName(String name);
}
