package com.xxf.apkmanagement.service.impl;


import com.xxf.apkmanagement.db.dao.TbDownloadStatisticsDao;
import com.xxf.apkmanagement.db.pojo.TbDownloadStatistics;
import com.xxf.apkmanagement.service.DownloadStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DownloadStatisticsServiceImpl implements DownloadStatisticsService {
    @Autowired
    private TbDownloadStatisticsDao tbDownloadStatisticsDao;

    @Override
    public int insert(TbDownloadStatistics tbDownloadStatistics) {
        return tbDownloadStatisticsDao.insertDownloadStatistics(tbDownloadStatistics);
    }

    @Override
    public TbDownloadStatistics updateDownloadStatistics(TbDownloadStatistics tbDownloadStatistics) {
        return tbDownloadStatisticsDao.updateDownloadStatistics(tbDownloadStatistics);
    }

    @Override
    public TbDownloadStatistics searchDownloadStatistics(TbDownloadStatistics tbDownloadStatistics) {
        return tbDownloadStatisticsDao.searchDownloadStatistics(tbDownloadStatistics);
    }
}
