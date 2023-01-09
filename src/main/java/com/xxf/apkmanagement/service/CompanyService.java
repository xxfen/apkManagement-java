package com.xxf.apkmanagement.service;

import com.xxf.apkmanagement.db.pojo.TbCompany;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface CompanyService {

     int insert(TbCompany company);

     TbCompany searchCompanyById(int userId);

     TbCompany searchCompanyByName(String name);

}
