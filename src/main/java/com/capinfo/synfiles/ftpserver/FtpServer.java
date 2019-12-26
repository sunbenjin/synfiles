package com.capinfo.synfiles.ftpserver;

import com.capinfo.synfiles.bean.FtpConfigInfo;
import com.capinfo.synfiles.utils.FTPUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FtpServer {

    private static FtpServer server = null;
    private static String directory = "";
    private FtpServerFactory serverFactory = null;
    private org.apache.ftpserver.FtpServer ftpServer = null;

    private FtpConfigInfo ftpConfigInfo;
    private FtpServer(String directory) throws FtpException {
        FtpServer.directory = directory;
        if(server==null){
            serverFactory = new FtpServerFactory();
            ListenerFactory factory = new ListenerFactory();
            //设置监听端口
            factory.setPort(2121);
            //替换默认监听
            serverFactory.addListener("default", factory.createListener());
            //用户名
            BaseUser user = new BaseUser();
            user.setName("admin");
            //密码 如果不设置密码就是匿名用户
            user.setPassword("capinfo123456");
            //用户主目录
            user.setHomeDirectory(directory);

            List<Authority> authorities = new ArrayList<Authority>();
            //增加写权限
            authorities.add(new WritePermission());
            user.setAuthorities(authorities);

            //增加该用户
            serverFactory.getUserManager().save(user);

            ftpServer = serverFactory.createServer();
        }
    }

    public void start(){
        try {
            //System.out.println(ftpServer.isStopped());
            ftpServer.start();
            //System.out.println(ftpServer.isStopped());
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        if(ftpServer!=null){
            ftpServer.stop();
            server = null;
        }
    }

    public boolean isStopped(){
        if (ftpServer!=null){
            return ftpServer.isStopped();
        }else{
            return true;
        }
    }

    /**
     * 获取相对地址 暂时没写
     * @return
     */
    public static String relativePath(){
        if(!StringUtils.isEmpty(directory)){

        }
        return directory;
    }

//    public static void main(String[] args) throws IOException {
//
//    }

    /**
     * 获取绝对地址
     * @return
     */
    public static String absolutelyPath(){
        return directory;
    }

    public static String dirPath(){
        return directory;
    }

    public static FtpServer getServer() {
        return server;
    }

    /**
     * 监控服务器是否开启
     *      true  表示关闭
     *      false 表示开启
     * @return
     */
    public boolean monitor(){
        if(server==null){
            return true;
        }else{
            if(ftpServer!=null){
                return ftpServer.isStopped();
            }
        }
        return true;
    }

    public static FtpServer newInstance(String directory) {
        System.out.println("FTP服务器对应的文件夹为:"+directory);
        if(!StringUtils.isEmpty(directory)){
             File files = new File(directory);
             if(!files.isDirectory()){
                 files.mkdirs();
             }
        }
        if(server==null){
            try {
                server = new FtpServer(directory);
            } catch (FtpException e) {
                e.printStackTrace();
            }
        }
        return server;
    }

}