package com.xxf.apkmanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system-params")
public class SystemParams {
    private String rootLocalPath;

    public String getRootLocalPath() {
        return rootLocalPath;
    }

    public void setRootLocalPath(String rootLocalPath) {
        this.rootLocalPath = rootLocalPath;
    }
}
