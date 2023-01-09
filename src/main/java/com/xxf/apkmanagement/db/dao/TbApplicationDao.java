package com.xxf.apkmanagement.db.dao;

import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbApk;
import com.xxf.apkmanagement.db.pojo.TbApplication;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbApplicationDao {
    ArrayList<AppApkModel> searchSelfApps(HashMap map);

    AppApkModel searchAppApkById(int id);

    AppApkModel searchAppApkByAppPackage(String appPackage);

    ArrayList<AppApkModel> searchCompanyApps(HashMap map);

    int searchAppCountByCompanyId(HashMap map);

    int searchAppCountByUserId(HashMap map);

    HashMap searchAppByPackage(String appPackage);

    HashMap searchAppById(int id);

    int insert(TbApplication tbApplication);
}
