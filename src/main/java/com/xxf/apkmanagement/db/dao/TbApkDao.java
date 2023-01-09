package com.xxf.apkmanagement.db.dao;

import com.xxf.apkmanagement.db.model.ApkListModel;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbApk;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbApkDao {
    int searchApkCountByUserId(int userId);

    int insert(TbApk apk);

    AppApkModel searchApkById(int id);

    ArrayList<ApkListModel> searchApkByAppPackageName(HashMap map);

    int searchApkByAppCountPackageName(HashMap map);
}
