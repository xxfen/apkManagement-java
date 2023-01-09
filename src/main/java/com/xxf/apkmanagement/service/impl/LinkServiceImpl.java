package com.xxf.apkmanagement.service.impl;

import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.db.dao.TbLinkDao;
import com.xxf.apkmanagement.db.model.LinkListModel;
import com.xxf.apkmanagement.db.pojo.TbLink;
import com.xxf.apkmanagement.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;


@Service
public class LinkServiceImpl implements LinkService {
    @Autowired
    private TbLinkDao tbLinkDao;

    @Override
    public int searchLinkCount(HashMap param) {
        return tbLinkDao.searchLinkCount(param);
    }

    @Override
    public PageUtils serchLinks(HashMap param) {
        ArrayList<LinkListModel> list = tbLinkDao.searchLinks(param);
        int start = (Integer) param.get("start");
        int length = (Integer) param.get("length");
        long count = searchLinkCountByUserId(param);
        PageUtils pageUtils = new PageUtils(list, count, start, length);
        return pageUtils;
    }

    @Override
    public int insertLink(HashMap param) {
        return tbLinkDao.insertLink(param);
    }

    @Override
    public ArrayList<TbLink> searchLinkByApkId(HashMap hashMap) {
        return tbLinkDao.searchLinkByApkId(hashMap);
    }

    @Override
    public TbLink searchLinkByLink(String link) {
        return tbLinkDao.searchLinkByLink(link);
    }

    @Override
    public int updateLink(HashMap param) {
        return tbLinkDao.updateLink(param);
    }

    @Override
    public int searchLinkCountByUserId(HashMap param) {
        return tbLinkDao.searchLinkCountByUserId(param);
    }
}
