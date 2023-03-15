package com.xxf.apkmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.common.utils.R;
import com.xxf.apkmanagement.config.SystemParams;
import com.xxf.apkmanagement.controller.form.SearchAllByUserIdForm;
import com.xxf.apkmanagement.controller.form.SearchAppByCompanyIdForm;
import com.xxf.apkmanagement.controller.form.UploadingAppForm;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.TbApk;
import com.xxf.apkmanagement.db.pojo.TbApplication;
import com.xxf.apkmanagement.db.pojo.TbCompany;
import com.xxf.apkmanagement.service.ApkService;
import com.xxf.apkmanagement.service.AppService;
import com.xxf.apkmanagement.service.CompanyService;
import com.xxf.apkmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/app")
@Tag(name = "应用程序接口")
public class AppController {
    private Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    SystemParams systemParams;
    @Autowired
    private AppService appService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ApkService apkService;


    @PostMapping("/searchCompanyAllApp")
    @Operation(summary = "查询公司下所有应用程序")
    @SaCheckRole("admin")
    @SaCheckLogin
    public R searchCompanyAllApp(@Valid @RequestBody SearchAppByCompanyIdForm form) {
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        HashMap hashMap = userService.searchUserById(userId);
        int companyId = (int) hashMap.get("companyId");
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("companyId", companyId);
        param.put("start", start);
        PageUtils pageUtils = appService.searchCompanyApps(param);
        return R.ok().put("page", pageUtils);
    }

    @PostMapping("/searchUserAllApp")
    @Operation(summary = "查询某个用户管理的所有应用程序")
    @SaCheckRole("admin")
    @SaCheckLogin
    public R searchUserAllApp(@Valid @RequestBody SearchAllByUserIdForm form) {
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("userId", userId);
        param.put("start", start);
        PageUtils pageUtils = appService.searchSelfApps(param);
        return R.ok().put("page", pageUtils);
    }


    @PostMapping("/uploadingApp")
    @Operation(summary = "上传应用")
    @SaCheckLogin
    @ResponseBody
    public R uploadingApp(@Valid UploadingAppForm form, @Valid @NonNull MultipartFile apk) {
        logger.info("--------------上传应用");
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        HashMap hashMap = userService.searchUserById(userId);
        int companyId = (int) hashMap.get("companyId");
        TbCompany company = companyService.searchCompanyById(companyId);
        String rootLocalPath = systemParams.getRootLocalPath() + "/";
        String filePath = company.getCompanyName() + "/";
        logger.info(filePath);
        logger.info(form.getContent());
        String companyPath = rootLocalPath + filePath;
        String temporary = companyPath + "temporary/";
        logger.info(temporary);
        File dir = new File(temporary);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        String apkName = System.currentTimeMillis() + apk.getOriginalFilename();
        ApkMeta apkMeta = null;
        try {
            /*先保存成一个临时文件*/
            File file = new File(temporary + apkName);
            FileUtils.copyInputStreamToFile(apk.getInputStream(), file);
            if (file.exists() && file.isFile()) {
                ApkFile apkTemFile = new ApkFile(file);
                apkMeta = apkTemFile.getApkMeta();//注释：apk所有信息都在apkMeta类里面。可以输出整个apkMeta来查看跟多详情信息
                logger.info("应用名称 :" + apkMeta.getLabel());
                logger.info("包名     :" + apkMeta.getPackageName());
                logger.info("版本号   :" + apkMeta.getVersionCode());
                logger.info("版本名称   :" + apkMeta.getVersionName());
                logger.info("图标     :" + apkMeta.getIcon());
                logger.info("大小     :" + (double) (file.length() * 100 / 1024 / 1024) / 100 + " MB");
                /*查询数据库是否已有该应用*/
                HashMap packMap = appService.searchAppByPackage(apkMeta.getPackageName());
                if (packMap != null) {
                    //存在该应用，提示去应用下上传安装包
                    apkTemFile.close();
                    //删除缓存文件
                    file.delete();
                    return R.error(300, "该应用已存在，如需应用版本更新请前去该应用下上传");
                }

                /*保存应用文件及数据*/
                //添加应用表
                TbApplication tbApplication = new TbApplication();
                tbApplication.setAppName(apkMeta.getLabel());
                tbApplication.setAppPackage(apkMeta.getPackageName());
                tbApplication.setUserId(userId);
                tbApplication.setCompanyId(companyId);
                int insert = appService.insert(tbApplication);

                //保存apk文件
                File fileSave = new File(companyPath + apkMeta.getPackageName() + "/");
                if (!fileSave.exists() && !fileSave.isDirectory()) {
                    fileSave.mkdirs();
                }
                apkName = apkMeta.getLabel() + "_" + System.currentTimeMillis() + "_" + apkMeta.getVersionName() + ".apk";
                File apkFile = new File(companyPath + apkMeta.getPackageName() + "/" + apkName);
                FileUtils.copyInputStreamToFile(apk.getInputStream(), apkFile);

                //添加安装包表
                TbApk tbApk = new TbApk();
                tbApk.setAppPackage(apkMeta.getPackageName());
                tbApk.setContent(form.getContent());
                tbApk.setForcedUpdating(form.isForcedUpdating());
                tbApk.setVCode(apkMeta.getVersionCode());
                tbApk.setVName(apkMeta.getVersionName());
                tbApk.setFileSize((apkFile.length() * 100 / 1024 / 1024) / 100 + " MB");
                tbApk.setApkPath(apkName);//文件路径
                apkService.insert(tbApk);
                apkTemFile.close();
                file.delete();
            }

        } catch (IOException e) {
            File file = new File(temporary + apkName);
            if (file.exists()) {
                file.delete();
            }
            e.printStackTrace();
        }
        return R.ok().put("app", apkName);
    }
}
