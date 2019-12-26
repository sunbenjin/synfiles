package com.capinfo.synfiles.listener;

import com.capinfo.synfiles.ftpserver.FtpClientUtils;
import com.capinfo.synfiles.ftpserver.FtpServer;
import com.capinfo.synfiles.model.FileInfo;
import com.capinfo.synfiles.utils.MessageUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileListener extends FileAlterationListenerAdaptor {

    private String rootDir;
    private static Logger LOGGER = LoggerFactory.getLogger(FileListener.class);

    public FileListener(String rootDir) {
        this.rootDir = rootDir;
    }



    /**
     * 文件创建
     * @param file
     */
    @Override
    public void onFileCreate(File file) {
        System.out.println("rootDir----"+this.rootDir);

        FileInfo info = new FileInfo(file);
        FtpClientUtils ftpClient = FtpClientUtils.getClient();
        boolean cp= false;
        String abrootDir=rootDir.substring(rootDir.indexOf(':')+1);
        System.out.println("adpath---"+abrootDir);
        System.out.println("really--"+info.getRelativePath().replace(abrootDir,""));
        cp = ftpClient.uploadLocalFile(getRealPath(info.getRelativePath()),file.getAbsolutePath(),info.getFileName());

        println("【新建】",file, cp);

    }

    private void println(String meesage,File file, boolean cp) {
        if (cp) {
            System.out.println(meesage+"成功" + file.getAbsolutePath());
            LOGGER.info(meesage+"成功" + file.getAbsolutePath());
            MessageUtils.println(meesage+"成功：" + file.getAbsolutePath() + "</br>");
        } else {
            System.out.println(meesage+"失败:" + file.getAbsolutePath());
            LOGGER.error(meesage+"成功" + file.getAbsolutePath());
            MessageUtils.println(meesage+"失败：" + file.getAbsolutePath() + "</br>");
        }
    }

    /**
     * 文件修改
     * @param file
     */
    @Override
    public void onFileChange(File file) {

        FileInfo info = new FileInfo(file);
        FtpClientUtils ftpClient = FtpClientUtils.getClient();
        boolean cp= ftpClient.uploadLocalFile(getRealPath(info.getRelativePath()),file.getAbsolutePath(),info.getFileName());
        println("【修改】",file, cp);
    }

    /**
     * 文件删除
     * @param file
     */
    @Override
    public void onFileDelete(File file) {
        FileInfo info = new FileInfo(file);
        FtpClientUtils ftpClient = FtpClientUtils.getClient();
        String realPath = getRealPath(info.getRelativePath());
        boolean cp = ftpClient.deleteFiles(realPath,realPath+"\\"+info.getFileName());
        //new FtpClientUtils("http://localhost");
        println("【删除】",file, cp);
    }

    private String getRealPath(String path){
        String abRootDir=rootDir.substring(rootDir.indexOf(':')+1);
        return  path.replace(abRootDir,"");

    }


}
