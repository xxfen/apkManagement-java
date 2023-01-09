package com.xxf.apkmanagement.service.impl;

import com.xxf.apkmanagement.db.dao.TbCompanyDao;
import com.xxf.apkmanagement.db.pojo.TbCompany;
import com.xxf.apkmanagement.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    public TbCompanyDao tbCompanyDao;

    @Override
    public int insert(TbCompany company) {
        int insert = tbCompanyDao.insert(company);
        return insert;
    }

    @Override
    public TbCompany searchCompanyById(int userId) {
        return tbCompanyDao.searchCompanyById(userId);
    }

    @Override
    public TbCompany searchCompanyByName(String name) {
        return tbCompanyDao.searchCompanyByName(name);
    }
}
