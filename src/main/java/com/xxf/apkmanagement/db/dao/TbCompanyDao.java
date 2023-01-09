package com.xxf.apkmanagement.db.dao;

import com.xxf.apkmanagement.db.pojo.TbCompany;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TbCompanyDao {
    int insert(TbCompany tbCompany);
    TbCompany searchCompanyById(int id);


    TbCompany searchCompanyByName(String name);
}
