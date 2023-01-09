package com.xxf.apkmanagement.db.dao;

import com.xxf.apkmanagement.db.model.LinkListModel;
import com.xxf.apkmanagement.db.pojo.TbLink;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface TbLinkDao {
    int searchLinkCount(HashMap param);

    ArrayList<LinkListModel> searchLinks(HashMap param);

    int insertLink(HashMap param);

    ArrayList<TbLink> searchLinkByApkId(HashMap param);

    TbLink searchLinkByLink(String link);

    int updateLink(HashMap param);

    int searchLinkCountByUserId(HashMap param);
}
