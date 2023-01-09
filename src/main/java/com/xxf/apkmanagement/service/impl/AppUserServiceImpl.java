package com.xxf.apkmanagement.service.impl;

import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.db.dao.TbAppUserDao;
import com.xxf.apkmanagement.db.dao.TbApplicationDao;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbAppUser;
import com.xxf.apkmanagement.db.pojo.TbApplication;
import com.xxf.apkmanagement.service.AppService;
import com.xxf.apkmanagement.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class AppUserServiceImpl implements AppUserService {
    @Autowired
    private TbAppUserDao tbAppUserDao;

    @Override
    public int insert(HashMap map) {
        return tbAppUserDao.insertAppUser(map);
    }

    @Override
    public TbAppUser searchAppUserByUserName(HashMap map) {
        return tbAppUserDao.searchAppUserByUserName(map);
    }


}
