package com.capinfo.synfiles.model;

import java.io.File;
import java.io.IOException;

/**
 * 分解文件信息
 */
public class FileInfo {
    /**
     *  文件名
     */
    private String fileName;
    /**
     * 相对路径
     */
    private String relativePath;
    /**
     * 获取盘符
     */
    private String diskChar;


    public FileInfo(File file) {
        String temps= null;
        try {
            temps = file.getCanonicalPath();
            String[] relativePaths = temps.split(":");
            String fileNameNow = temps.substring(temps.lastIndexOf("\\")+1);
            this.fileName = fileNameNow.trim();
            this.relativePath = file.getParent().trim().split(":")[1];
            this.diskChar = relativePaths[0].trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getDiskChar() {
        return diskChar;
    }

}
