package com.xxf.apkmanagement.service;

import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.db.model.ApkListModel;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbApk;

import java.util.ArrayList;
import java.util.HashMap;

public interface ApkService {

    int searchApkCountByUserId(int userId);

    AppApkModel searchApkById(int id);

    int insert(TbApk apk);

    PageUtils searchApkByAppPackageName(HashMap packageName);

}
