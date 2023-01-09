package com.xxf.apkmanagement.service;

import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.db.model.LinkListModel;
import com.xxf.apkmanagement.db.pojo.TbLink;

import java.util.ArrayList;
import java.util.HashMap;

public interface LinkService {

    int searchLinkCount(HashMap param);

    PageUtils serchLinks(HashMap param);

    int insertLink(HashMap param);

    ArrayList<TbLink> searchLinkByApkId(HashMap hashMap);

    TbLink searchLinkByLink(String link);

    int updateLink(HashMap param);

    int searchLinkCountByUserId(HashMap param);
}
