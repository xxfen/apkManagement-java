package com.xxf.apkmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.xxf.apkmanagement.common.utils.R;
import com.xxf.apkmanagement.controller.form.*;
import com.xxf.apkmanagement.db.model.UserListModel;
import com.xxf.apkmanagement.db.pojo.TbCompany;
import com.xxf.apkmanagement.db.pojo.TbUser;
import com.xxf.apkmanagement.service.CompanyService;
import com.xxf.apkmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户接口")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "登陆系统")
    public R login(@Valid @RequestBody LoginForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        Integer userId = userService.login(param);
        R r = R.ok().put("result", userId != null ? true : false);
        logger.info("登陆:"+userId);
        if (userId != null) {
//            StpUtil.login(userId);
            StpUtil.login(userId);
            List<String> role = userService.getRoleList(userId);
            /*
             * 因为新版的Chrome浏览器不支持前端Ajax的withCredentials，
             * 导致Ajax无法提交Cookie，所以我们要取出生成的Token返回给前端，
             * 让前端保存在Storage中，然后每次在Ajax的Header上提交Token
             */
            String token = StpUtil.getTokenInfo().getTokenValue();
            r.put("role", role.get(0)).put("token", token);
        }
        return r;
    }
    @GetMapping("/loginOut")
    @Operation(summary = "退出登录")
    @SaCheckLogin
    public R loginOut() {
        StpUtil.logout();
        return R.ok().put("result", true);
    }



    @PostMapping("/checCode")
    @Operation(summary = "检测登陆验证码")
    public R checCode(@Valid @RequestBody CheckCodeForm form) {
        return R.ok().put("result", true);
    }


    @GetMapping("/searchAllUser")
    @Operation(summary = "查询公司所有用户")
    @SaCheckRole("admin")
    @SaCheckLogin
    public R searchAllUser() {
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        HashMap hashMap = userService.searchUserById(userId);
        int companyId = (int) hashMap.get("companyId");
        ArrayList<UserListModel> list = userService.searchAllUser(companyId);
        return R.ok().put("list", list);
    }


    @PostMapping("/register")
    @Operation(summary = "管理员注册")
    public R register(@Valid @RequestBody RegisterForm form) {
        //查询用户名是否存在
        TbUser user = userService.searchUserByName(form.getUserName());
        if (user!=null){
            return R.error("该用户名已存在");
        }
        //查询公司名是否存在
        TbCompany company1 = companyService.searchCompanyByName(form.getCompanyName());
        if (company1!=null){
            return R.error("该公司名已存在");
        }
        TbCompany company = new TbCompany();
        company.setCompanyName(form.getCompanyName());
        int insert = companyService.insert(company);
        logger.info("公司Id："+company.getId());
        TbUser tbUser = JSONUtil.parseObj(form).toBean(TbUser.class);
        tbUser.setCompanyId(company.getId());
        int rows = userService.insert(tbUser);
        return R.ok().put("rows", rows);

    }

    @PostMapping("/addUser")
    @Operation(summary = "添加用户")
    @SaCheckRole("admin")
    @SaCheckLogin
    public R addUser(@Valid @RequestBody AddUserForm form) {
        TbUser tbUser = JSONUtil.parseObj(form).toBean(TbUser.class);
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        HashMap hashMap = userService.searchUserById(userId);
        logger.info(""+ hashMap.size());
        logger.info("公司Id："+hashMap.get("companyId"));
        int companyId =   Integer.parseInt(hashMap.get("companyId").toString());
        logger.info("公司Id："+companyId);
        tbUser.setCompanyId(companyId);
        int rows = userService.insert(tbUser);
        return R.ok().put("rows", rows);
    }

}

