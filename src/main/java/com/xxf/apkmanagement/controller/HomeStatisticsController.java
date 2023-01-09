package com.xxf.apkmanagement.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.xxf.apkmanagement.common.utils.R;
import com.xxf.apkmanagement.service.ApkService;
import com.xxf.apkmanagement.service.AppService;
import com.xxf.apkmanagement.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/home")
@Tag(name = "首页统计接口")
public class HomeStatisticsController {
    @Autowired
    private AppService appService;
    @Autowired
    private ApkService apkService;
    @Autowired
    private LinkService linkService;

    @GetMapping("/statisticsCount")
    @Operation(summary = "数量统计")
    @SaCheckLogin
    public R  statisticsCount(){
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        HashMap map=new HashMap();
        map.put("userId",userId);
        int appCount = appService.searchAppCountByUserId(map);
        int apkCount = apkService.searchApkCountByUserId(userId);
        HashMap hashMap=new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("disabled",false);
        int linkCount = linkService.searchLinkCount(hashMap);

        return R.ok()
                .put("appCount", appCount)
                .put("apkCount", apkCount)
                .put("linkCount", linkCount);
    }

}
