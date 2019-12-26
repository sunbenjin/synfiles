package com.capinfo.synfiles;

import com.capinfo.synfiles.controller.ServerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.net.URL;

@EnableScheduling  //开启
@SpringBootApplication
public class SynfilesApplication {


    private static Logger LOGGER = LoggerFactory.getLogger(SynfilesApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(SynfilesApplication.class, args);

//        URL url = SynfilesApplication.class.getResource("/");
//        File file = new File(url.getPath());
//        System.out.println(file.getAbsolutePath());

        //String privateKeyFileStr = request.getSession().getServletContext().getRealPath("RSA/privateKey.txt")
       //String cmd = file.getAbsolutePath()+"/chrome/chrome.exe http://localhost:8080";
//        LOGGER.info("路径:"+args[0]);
//        System.out.println(args[0]);
        //String cmd = args[0]+"/chrome/chrome.exe http://localhost:8080";
        String cmd = "D:\\idea\\synfiles\\src\\main\\resources\\chrome\\chrome.exe http://localhost:8080";
//        String cmd = "C:/Users/cs001/AppData/Local/Google/Chrome/Application/chrome.exe http://localhost:8080";
        LOGGER.info("路径:"+cmd);
        LOGGER.error("路径:"+cmd);
        LOGGER.debug("路径:"+cmd);
        Runtime run = Runtime.getRuntime();
        try{
            run.exec(cmd);
            LOGGER.debug("启动浏览器打开项目成功");
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }



}

