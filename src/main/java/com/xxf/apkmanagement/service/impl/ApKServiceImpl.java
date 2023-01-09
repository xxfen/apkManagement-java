package com.xxf.apkmanagement.service.impl;

import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.db.dao.TbApkDao;
import com.xxf.apkmanagement.db.model.ApkListModel;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbApk;
import com.xxf.apkmanagement.service.ApkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;


@Service
public class ApKServiceImpl implements ApkService {
    @Autowired
    private TbApkDao tbApk;

    @Override
    public int searchApkCountByUserId(int userId) {
        int i = tbApk.searchApkCountByUserId(userId);
        return i;
    }

    @Override
    public AppApkModel searchApkById(int id) {
        return tbApk.searchApkById(id);
    }

    @Override
    public int insert(TbApk apk) {
        int insert = tbApk.insert(apk);
        return insert;
    }

    @Override
    public PageUtils searchApkByAppPackageName(HashMap param) {
        ArrayList<ApkListModel> list = tbApk.searchApkByAppPackageName(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        long count = tbApk.searchApkByAppCountPackageName(param);
        PageUtils pageUtils = new PageUtils(list, count, start, length);
        return pageUtils;
    }

}
