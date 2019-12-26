package com.capinfo.synfiles.utils;

import com.capinfo.synfiles.bean.FtpConfigInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class FTPUtils {
    protected static Logger logger = LoggerFactory.getLogger(FTPUtils.class);

    /**
     * 连接Ftp
     * @param configInfo
     * @return
     */
    public static FTPClient connectServer(FtpConfigInfo configInfo){
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(5000);
        ftpClient.setDataTimeout(10*1000);

        try {
            ftpClient.connect(configInfo.getServer(),configInfo.getPort());
        } catch (IOException e) {
            logger.info("ftp服务器连接超时，请检查ftp服务器是否正确【ftpServer:"+configInfo.getServer()+" 】【port："+configInfo.getPort()+"】【user:"+configInfo.getUser()+"】【password:"+configInfo.getPassword()+"】");
            e.printStackTrace();
        }

        try {
            if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
                if(ftpClient.login(configInfo.getUser(),configInfo.getPassword())){
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    ftpClient.sendCommand("FEAT");
                    String featRS = ftpClient.getReplyString();
                    String encoding = "GBK";
                    if(featRS.indexOf("UTF8")!=-1){
                        encoding="UTF8";
                    }
                    if(configInfo.getLocation() !=null){
                        configInfo.setLocation(new String(configInfo.getLocation().getBytes(encoding),"ISO-8859-1"));
                    }
                    if(configInfo.getFileName() !=null){
                        configInfo.setFileName(new String(configInfo.getFileName().getBytes(encoding),"ISO-8859-1"));
                    }
                    configInfo.setEncoding(encoding);
                    ftpClient.setActivePortRange(9000,9100);
                    return ftpClient;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
