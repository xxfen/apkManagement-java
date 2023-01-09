package com.xxf.apkmanagement.service;

import com.xxf.apkmanagement.db.model.UserListModel;
import com.xxf.apkmanagement.db.pojo.TbUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface UserService {

    public ArrayList<UserListModel> searchAllUser(int companyId);

    public List<String> getRoleList(int userId);

    public Integer login(HashMap param);

    int insert(TbUser user);

    HashMap searchUserById(int userId);

    TbUser searchUserByName(String name);
}
