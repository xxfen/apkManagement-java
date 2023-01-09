package com.xxf.apkmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

@ServletComponentScan
@SpringBootApplication
public class ApkManagementApplication extends SpringBootServletInitializer {

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApkManagementApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApkManagementApplication.class, args);
    }
    /**
     * 查看用了什么数据库连接池
     */
    public void run(String... args) throws Exception {
        System.out.println("DATASOURCE = " + dataSource + "-00011");
    }
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //factory.setLocation(uploadFolder);
//		//文件最大
//		factory.setMaxFileSize( DataSize.ofMegabytes(2));
//		// 设置总上传数据总大小
//		factory.setMaxRequestSize(DataSize.ofMegabytes(4));
        //单个文件大小
        factory.setMaxFileSize(DataSize.ofMegabytes(10L));
        //设置总上传数据大小
        factory.setMaxRequestSize(DataSize.ofMegabytes(50L));
        return factory.createMultipartConfig();
    }
}
