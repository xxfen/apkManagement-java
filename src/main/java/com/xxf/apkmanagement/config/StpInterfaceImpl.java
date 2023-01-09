package com.xxf.apkmanagement.config;

import cn.dev33.satoken.stp.StpInterface;

import com.xxf.apkmanagement.db.dao.TbUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private TbUserDao userDao;

    /**
     * 返回一个用户所拥有的权限集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        int userId = Integer.parseInt(loginId.toString());
        //因为本项目不需要用到用户所拥有的权限判定，所以这里就返回一个空的ArrayList对象
        ArrayList<String> list = new ArrayList<String>();
        return list;
    }

    /**
     * 返回一个用户所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        int userId = Integer.parseInt(loginId.toString());
        boolean root = userDao.getRole(userId);
        ArrayList<String> list = new ArrayList<String>();
        list.add(root?"admin":"user");
        return list;
    }
}

