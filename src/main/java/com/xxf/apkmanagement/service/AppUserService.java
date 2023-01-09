package com.xxf.apkmanagement.service;

import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.db.dao.TbAppUserDao;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbAppUser;
import com.xxf.apkmanagement.db.pojo.TbApplication;

import java.util.HashMap;

public interface AppUserService {

    int insert(HashMap map);

    TbAppUser searchAppUserByUserName(HashMap map);
}
