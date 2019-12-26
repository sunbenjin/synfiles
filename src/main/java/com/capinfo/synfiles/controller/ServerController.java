package com.capinfo.synfiles.controller;

import com.capinfo.synfiles.bean.FtpConfigInfo;
import com.capinfo.synfiles.ftpserver.FileDirMonitorServer;
import com.capinfo.synfiles.ftpserver.FtpClientUtils;
import com.capinfo.synfiles.ftpserver.FtpServer;
import com.capinfo.synfiles.listener.FileListener;
import com.capinfo.synfiles.model.ResultBean;
import com.capinfo.synfiles.utils.FTPUtils;
import com.capinfo.synfiles.utils.MessageUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/ftpServer")
public class ServerController {

    private static Logger LOGGER = LoggerFactory.getLogger(ServerController.class);


    @RequestMapping(value="/start", method = RequestMethod.GET)
    public ResultBean startftpServer(HttpServletRequest request, HttpServletResponse response){
        try {

            String ftpAddress = request.getParameter("ftpAddress");
            if(StringUtils.isEmpty(ftpAddress)){
                LOGGER.error("未显示服务器地址");
                return ResultBean.fail("未显示服务器地址");
            }
            if(ftpAddress.toUpperCase().indexOf("FTP")!=-1){
                 LOGGER.info("FTP远程服务器连接");
                FtpClientUtils clientUtils = FtpClientUtils.newInstance(ftpAddress);

                //FTPClient
                //FTPClient ftpClient = FTPUtils.connectServer(configInfo);
                //FtpServer ftpServer =FtpServer.newInstance(configInfo);
              if(clientUtils==null){
                    return ResultBean.fail("连接ftp服务器失败");
                }
             if(clientUtils.loginSuccess){
                 return ResultBean.ok("FTP服务器已经开启");
             }else{
                 return ResultBean.fail("连接ftp服务器失败");
             }

            }
            FtpServer ftpServer =FtpServer.newInstance(ftpAddress);
//            System.out.println(ftpServer.isStopped());
            if (ftpServer.isStopped()) {
                LOGGER.info("FTP服务器已经开启");
                System.out.println("FTP服务器已经开启");
                ftpServer.start();
                FtpClientUtils.newInstance("");
            }else{
                LOGGER.info("FTP服务器已经关闭");
                System.out.println("FTP服务器已经关闭");
                ftpServer.stop();
            }
            return ResultBean.ok("服务器已经开启");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("服务器开启失败"+e.getMessage());
            return ResultBean.fail("服务器开启失败");
        }
    }



    @RequestMapping(value="/close", method = RequestMethod.GET)
    public ResultBean closeftpServer(HttpServletRequest request, HttpServletResponse response){
        try {
            FtpServer ftpServer =FtpServer.newInstance(request.getParameter("ftpAddress"));
            if (!ftpServer.isStopped()) {
                System.out.println("FTP服务器已经关闭");
                ftpServer.stop();
            }
            return ResultBean.ok("FTP服务器已经关闭");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.fail("服务器开启失败");
        }
    }

    @RequestMapping(value="/openMonitor",method=RequestMethod.GET)
    public ResultBean openDirMonitor(HttpServletRequest request, HttpServletResponse response){
        Set<String> pathDir = new HashSet<String>();
        String rootDirs= request.getParameter("rootDirs");
        if (!StringUtils.isEmpty(rootDirs)){
            String[] roots = rootDirs.split(";");
            for (String root : roots) {
                String tempFileDir = root.toLowerCase();
                File file = new File(tempFileDir);
                if(file.isDirectory()){
                    pathDir.add(tempFileDir);
                }else{
                    MessageUtils.println(tempFileDir+"");//不是
                }
            }
            try {
                FileDirMonitorServer dir = FileDirMonitorServer.newInstance(pathDir);
                dir.stopAll();
                dir.start();
                return ResultBean.ok("开启完成");
            } catch (Exception e) {
                e.printStackTrace();
                return ResultBean.fail("开启失败");
            }
        }else{
            return ResultBean.fail("不能为空");
        }



    }



    @RequestMapping(value="/closeMonitor",method=RequestMethod.GET)
    public ResultBean stopDirMonitor(HttpServletRequest request,HttpServletResponse response){
        try {
            FileDirMonitorServer dir = FileDirMonitorServer.getServer();
            if(dir!=null){
                dir.stopAll();
            }
            return ResultBean.ok("取消监控完成");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.fail("取消监控完成");
        }
    }










}
