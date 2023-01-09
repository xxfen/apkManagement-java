package com.xxf.apkmanagement.service;

import com.xxf.apkmanagement.db.pojo.TbAppUser;
import com.xxf.apkmanagement.db.pojo.TbDownloadStatistics;

import java.util.HashMap;

public interface DownloadStatisticsService {
    int insert(TbDownloadStatistics tbDownloadStatistics);

    TbDownloadStatistics updateDownloadStatistics(TbDownloadStatistics tbDownloadStatistics);

    TbDownloadStatistics searchDownloadStatistics(TbDownloadStatistics tbDownloadStatistics);
}
