package com.xxf.apkmanagement.service.impl;

import com.xxf.apkmanagement.db.dao.TbUserDao;
import com.xxf.apkmanagement.db.model.UserListModel;
import com.xxf.apkmanagement.db.pojo.TbUser;
import com.xxf.apkmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserDao userDao;

    @Override
    public ArrayList<UserListModel> searchAllUser(int compannyId) {
        ArrayList<UserListModel> list = userDao.searchAllUser(compannyId);
        return list;
    }

    @Override
    public List<String> getRoleList(int userId) {
        boolean root = userDao.getRole(userId);
        ArrayList list = new ArrayList();
        list.add(root ? "admin" : "user");
        return list;
    }

    @Override
    public Integer login(HashMap param) {
        Integer userId = userDao.login(param);
        return userId;
    }

    @Override
    public int insert(TbUser user) {
        int insert = userDao.insert(user);
        return insert;
    }

    @Override
    public HashMap searchUserById(int userId) {
        HashMap hashMap = userDao.searchUserById(userId);
        return hashMap;
    }

    @Override
    public TbUser searchUserByName(String name) {
        return userDao.searchUserByName(name);
    }


}
