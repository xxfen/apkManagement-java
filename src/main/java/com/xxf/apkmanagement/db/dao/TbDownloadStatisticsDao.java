package com.xxf.apkmanagement.db.dao;

import com.xxf.apkmanagement.db.pojo.TbDownloadStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TbDownloadStatisticsDao {
    int insertDownloadStatistics(TbDownloadStatistics tbDownloadStatistics);

    TbDownloadStatistics updateDownloadStatistics(TbDownloadStatistics tbDownloadStatistics);

    TbDownloadStatistics searchDownloadStatistics(TbDownloadStatistics tbDownloadStatistics);
}
