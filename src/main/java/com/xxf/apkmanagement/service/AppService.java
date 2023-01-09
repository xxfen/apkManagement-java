package com.xxf.apkmanagement.service;

import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbApplication;

import java.util.ArrayList;
import java.util.HashMap;

public interface AppService {
    PageUtils searchSelfApps(HashMap map);

    PageUtils searchCompanyApps(HashMap map);

    int searchAppCountByUserId(HashMap map);

    HashMap searchAppByPackage(String appPackage);

    HashMap searchAppById(int id);

    int insert(TbApplication tbApplication);

    AppApkModel searchAppApkById(int id);

    AppApkModel searchAppApkByPackage(String appPackage);
}
