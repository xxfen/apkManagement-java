package com.xxf.apkmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.common.utils.R;
import com.xxf.apkmanagement.config.SystemParams;
import com.xxf.apkmanagement.controller.form.SearchApkByAppIdForm;
import com.xxf.apkmanagement.controller.form.UploadingAppForm;
import com.xxf.apkmanagement.db.model.ApkListModel;
import com.xxf.apkmanagement.db.model.ApkUpdateModel;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.pojo.*;
import com.xxf.apkmanagement.service.*;
import com.xxf.apkmanagement.util.AESUtil;
import com.xxf.apkmanagement.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/apk")
@Tag(name = "安装包接口")
public class ApkController {
    private Logger logger = LoggerFactory.getLogger(ApkController.class);
    @Autowired
    SystemParams systemParams;
    @Autowired
    private ApkService apkService;
    @Autowired
    private AppService appService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private DownloadStatisticsService downloadStatisticsService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;

    @PostMapping("/searchApk")
    @Operation(summary = "安装包列表")
    @SaCheckLogin
    public R searchLinks(@Valid @RequestBody SearchApkByAppIdForm form) {

        HashMap hashMap = appService.searchAppById(form.getId());
        String appPackage = (String) hashMap.get("appPackage");
        logger.info(appPackage);
//        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("start", start);
        param.put("appPackage", appPackage);
        PageUtils pageUtils = apkService.searchApkByAppPackageName(param);
        return R.ok().put("page", pageUtils);
    }

    @GetMapping("/searchAppApkById")
    @Operation(summary = "应用最新安装包信息")
    @SaCheckLogin
    //@ResponseBody
    public R searchAppApkById(@Valid @Schema(description = "应用安装包信息") @Param(value = "id") int id) throws UnsupportedEncodingException {
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        AppApkModel apkModel = appService.searchAppApkById(id);
        String encrypt = AESUtil.encrypt(apkModel.getAppPackage());
        String encode = URLEncoder.encode(encrypt, "UTF-8");
        String encode1 = URLEncoder.encode(encode, "UTF-8");
        apkModel.setUrl(/*util.getUrl() + */"apk/" + encode1);
        return R.ok().put("data", apkModel).put("update", userId == apkModel.getUserId());
    }

    //检查更新
    @GetMapping("/{name}")
    @Operation(summary = "检查更新")
    public R searchIsNewApk(@PathVariable("name") String name, @Min(value = 0) @Param(value = "vCode") int vCode) throws UnsupportedEncodingException {
        logger.info(name);
        String decode = URLDecoder.decode(name, "UTF-8");
        logger.info(decode);
        String decrypt = AESUtil.decrypt(decode);
        logger.info(decrypt);
        AppApkModel appApkModel = appService.searchAppApkByPackage(decrypt);

        if (vCode < appApkModel.getVCode()) {//有更新
            ApkUpdateModel model = new ApkUpdateModel();
            Util util = new Util();
            model.setContent(appApkModel.getContent());
            model.setFileSize(appApkModel.getFileSize());
            model.setVName(appApkModel.getVName());
            model.setVCode(appApkModel.getVCode());
            model.setMust(appApkModel.isForcedUpdating());
            String encrypt = AESUtil.encrypt(appApkModel.getId() + "");
            String encode = URLEncoder.encode(encrypt, "UTF-8");
            model.setUrl(/*util.getUrl()+*/"apk/download/" + encode);
            return R.ok().put("data", model);
        } else {
            return R.ok().put("data", null);
        }
    }


    /**
     * 安装包下载
     *
     * @param response
     * @param name
     * @return
     * @throws UnsupportedEncodingException
     */
    @Operation(summary = "安装包下载")
    @RequestMapping(value = "/download/{path}", method = {RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String downReport(HttpServletResponse response, @PathVariable String path, @NonNull @Param(value = "name") String name) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(path, "UTF-8");
        String decrypt = AESUtil.decrypt(decode);
        AppApkModel tbApk = apkService.searchApkById(Integer.parseInt(decrypt));

        String filepath = systemParams.getRootLocalPath() + "/" + tbApk.getCompanyName() + "/" + tbApk.getAppPackage() + "/" + tbApk.getApkPath();
        logger.info(name);
        File file = new File(filepath);
        // 如果文件存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            //response.setCharacterEncoding("UTF-8");
            // response.addHeader("Accept-Encoding","identity");
            response.setContentLengthLong(file.length());

            // 实现文件下载
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }

                HashMap map = new HashMap();
                map.put("appPackage", tbApk.getAppPackage());
                map.put("appUserName", name);
                TbAppUser tbAppUser = appUserService.searchAppUserByUserName(map);
                if (tbAppUser == null) {//添加用户，添加用户下载记录
                    logger.info("添加用户");
                    int insert = appUserService.insert(map);
                    TbDownloadStatistics downloadStatistics = new TbDownloadStatistics();
                    downloadStatistics.setApkId(tbApk.getId());
                    logger.info("添加用户下载记录");
                    downloadStatistics.setAppUserId(Integer.valueOf(map.get("id").toString()));
                    logger.info(map.get("id").toString());
                    logger.info(downloadStatistics.getApkId() + "");
                    logger.info(downloadStatistics.getAppUserId() + "");
                    downloadStatisticsService.insert(downloadStatistics);
                } else {//更新用户下载记录
                    logger.info("更新用户下载记录");
                    TbDownloadStatistics downloadStatistics = new TbDownloadStatistics();
                    downloadStatistics.setApkId(tbApk.getId());
                    downloadStatistics.setAppUserId(tbAppUser.getId());
                    TbDownloadStatistics downloadStatistics1 = downloadStatisticsService.searchDownloadStatistics(downloadStatistics);
                    downloadStatistics.setId(downloadStatistics1.getId());
                    assert downloadStatistics1 != null;
                    downloadStatistics.setCount(downloadStatistics1.getCount() + 1);
                    downloadStatisticsService.updateDownloadStatistics(downloadStatistics);
                }

                return "successfully";

            } catch (Exception e) {
                System.out.println("Download  failed!" + e.getMessage());
                return "failed";

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            response.setStatus(401);
            return "文件不存在";
        }
    }


    @PostMapping("/uploadingNewApp")
    @Operation(summary = "上传新版本")
    @SaCheckLogin
    @ResponseBody
    public R uploadingApp(@Valid UploadingAppForm form, @Valid @NonNull MultipartFile apk) {
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


                /*查询数据库是否有该应用*/
                AppApkModel appApkModel = appService.searchAppApkById(form.getAppId());

                if (appApkModel == null) {
                    apkTemFile.close();
                    //删除缓存文件
                    file.delete();
                    return R.error("appId错误");
                }
                if (appApkModel.getUserId() != userId) {

                    apkTemFile.close();
                    //删除缓存文件
                    file.delete();
                    return R.error("您没有权限");
                }
                if (!appApkModel.getAppPackage().equals(apkMeta.getPackageName())) {
                    apkTemFile.close();
                    //删除缓存文件
                    file.delete();
                    return R.error("apk包名不一致");
                }
                //查询数据库最新版本
                AppApkModel app = appService.searchAppApkByPackage(apkMeta.getPackageName());
                if (app.getVCode() >= apkMeta.getVersionCode()) {
                    apkTemFile.close();
                    //删除缓存文件
                    file.delete();
                    return R.error("该上传版本不是最新版本");
                }

                /*保存应用文件及数据*/
                //添加应用表
               /* TbApplication tbApplication = new TbApplication();
                tbApplication.setAppName(apkMeta.getLabel());
                tbApplication.setAppPackage(apkMeta.getPackageName());
                tbApplication.setUserId(userId);
                tbApplication.setCompanyId(companyId);
                int insert = appService.insert(tbApplication);*/

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


