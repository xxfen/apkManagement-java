package com.xxf.apkmanagement.service.impl;

import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.db.dao.TbApplicationDao;
import com.xxf.apkmanagement.db.model.ApkListModel;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbApplication;
import com.xxf.apkmanagement.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private TbApplicationDao tbApplicationDao;

    @Override
    public PageUtils searchSelfApps(HashMap param) {
        ArrayList<AppApkModel> list = tbApplicationDao.searchSelfApps(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        long count = tbApplicationDao.searchAppCountByUserId(param);
        PageUtils pageUtils = new PageUtils(list, count, start, length);
        return pageUtils;
    }

    @Override
    public PageUtils searchCompanyApps(HashMap param) {
        ArrayList<AppApkModel> list = tbApplicationDao.searchCompanyApps(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        long count = tbApplicationDao.searchAppCountByCompanyId(param);
        PageUtils pageUtils = new PageUtils(list, count, start, length);
        return pageUtils;
    }

    @Override
    public int searchAppCountByUserId(HashMap param) {
        return tbApplicationDao.searchAppCountByUserId(param);
    }

    @Override
    public HashMap searchAppByPackage(String appPackage) {

        return tbApplicationDao.searchAppByPackage(appPackage);
    }

    @Override
    public HashMap searchAppById(int id) {
        return tbApplicationDao.searchAppById(id);
    }

    @Override
    public int insert(TbApplication tbApplication) {
        int insert = tbApplicationDao.insert(tbApplication);
        return insert;
    }

    @Override
    public AppApkModel searchAppApkById(int id) {
        return tbApplicationDao.searchAppApkById(id);
    }

    @Override
    public AppApkModel searchAppApkByPackage(String appPackage) {
        return tbApplicationDao.searchAppApkByAppPackage(appPackage);
    }
}
