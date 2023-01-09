package com.xxf.apkmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.xxf.apkmanagement.common.utils.PageUtils;
import com.xxf.apkmanagement.common.utils.R;
import com.xxf.apkmanagement.config.SystemParams;
import com.xxf.apkmanagement.controller.form.CreateLinkForm;
import com.xxf.apkmanagement.controller.form.SearchLinkByApkIdFrom;
import com.xxf.apkmanagement.controller.form.SearchLinksForm;
import com.xxf.apkmanagement.db.model.AppApkModel;
import com.xxf.apkmanagement.db.model.LinkListModel;
import com.xxf.apkmanagement.db.pojo.TbLink;
import com.xxf.apkmanagement.service.*;
import com.xxf.apkmanagement.util.AESUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/link")
@Tag(name = "链接接口")
public class LinkController {
    private Logger logger = LoggerFactory.getLogger(LinkController.class);
    @Autowired
    SystemParams systemParams;
    @Autowired
    private LinkService linkService;
    @Autowired
    private ApkService apkService;


    @PostMapping("/searchLinks")
    @Operation(summary = "链接分页列表")
    @SaCheckLogin
    public R searchLinks(@Valid @RequestBody SearchLinksForm form) {
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;

        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("userId", userId);
        param.put("start", start);
        PageUtils pageUtils = linkService.serchLinks(param);
        return R.ok().put("page", pageUtils);
    }

    @PostMapping("/searchLinkByApkId")
    @Operation(summary = "查询某个安装包的链接")
    @SaCheckLogin
    public R searchLinkByApkId(@Valid @RequestBody SearchLinkByApkIdFrom form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("disabled", false);
        ArrayList<TbLink> tbLink = linkService.searchLinkByApkId(param);
        return R.ok().put("list", tbLink);
    }

    @PostMapping("/createLink")
    @Operation(summary = "链接生成")
    @SaCheckLogin
    public R createLink(@Valid @RequestBody CreateLinkForm form) {
        AppApkModel appApkModel = apkService.searchApkById(form.getApkId());
        if (appApkModel == null) {
            return R.error("安装包id不存在");
        }
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("userId", userId);
//        param.put("vName", appApkModel.getVName());
        String uuid = UUID.randomUUID().toString();
        param.put("link", "link/download/" + uuid);
        int i = linkService.insertLink(param);
        return R.ok().put("data", param).put("exist", false);
    }

    //链接修改

    //链接下载
    @GetMapping("/download/{uuid}")
    @Operation(summary = "链接下载")
    public String downloadLink(HttpServletResponse response, @PathVariable String uuid) throws UnsupportedEncodingException {
        TbLink tbLink = linkService.searchLinkByLink("link/download/" + uuid);
        if (tbLink == null) {
            return JSONObject.toJSONString(R.error(404, "链接不存在！"));
        }
        if (!tbLink.isDisabled()) {
            if (tbLink.getDisabledCount() > 0 && tbLink.getDisabledCount() <= tbLink.getCount()) {
                //链接状态设置为失效
                HashMap map = new HashMap();
                map.put("id", tbLink.getId());
                map.put("disabled", true);
                linkService.updateLink(map);
                return JSONObject.toJSONString(R.error(404, "链接下载次数已用完，已失效！"));
            }

            if (tbLink.getDeadTime().before(new Timestamp(System.currentTimeMillis()))) {
                //链接状态设置为失效
                HashMap map = new HashMap();
                map.put("id", tbLink.getId());
                map.put("disabled", true);
                linkService.updateLink(map);
                return JSONObject.toJSONString(R.error(404, "链接超时，已失效！"));
            }
        } else {
            return JSONObject.toJSONString(R.error(404, "链接已失效！"));
        }

        AppApkModel tbApk = apkService.searchApkById(tbLink.getApkId());
        String filepath = systemParams.getRootLocalPath() + "/" + tbApk.getCompanyName() + "/" + tbApk.getAppPackage() + "/" + tbApk.getApkPath();
        File file = new File(filepath);
        // 如果文件存在，则进行下载
        if (file.exists()) {
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
//            response.addHeader("Content-Length", "" + file.length());
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            //response.setCharacterEncoding("UTF-8");
            // response.addHeader("Accept-Encoding","identity");
//            response.setContentLengthLong(file.length());
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());

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
                //更新链接次数及状态
                if (tbLink.getDisabledCount() > 0 && tbLink.getDisabledCount() == tbLink.getCount() + 1) {
                    //链接状态设置为失效
                    logger.info("链接状态设置为失效!");
                    HashMap map = new HashMap();
                    map.put("disabled", true);
                    map.put("id", tbLink.getId());
                    map.put("count", tbLink.getCount() + 1);
                    linkService.updateLink(map);
                } else {
                    logger.info("更新下载次数!");
                    //更新下载次数
                    HashMap map = new HashMap();
                    map.put("id", tbLink.getId());
                    map.put("count", tbLink.getCount() + 1);
                    linkService.updateLink(map);
                }
                logger.info("下载成功!");
                return JSONObject.toJSONString(R.ok());

            } catch (Exception e) {
                System.out.println("Download  failed!" + e.getMessage());
                return JSONObject.toJSONString(R.error("下载失败"));

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
            return JSONObject.toJSONString(R.error(404, "文件不存在"));
        }
    }
}
