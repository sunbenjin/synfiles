package com.capinfo.synfiles.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class FileTestUtils {

    public static File newRandomFile(String path) {
        System.out.println(path);
        File file = new File(path);
        try {
            if (!file.exists()) {    //如果不存在data.txt文件则创建
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);        //创建文件写入
            BufferedWriter bw = new BufferedWriter(fw);

            //产生随机数据，写入文件
            Random random = new Random();
            for (int i = 0; i < 10000; i++) {
                int randint = (int) Math.floor((random.nextDouble() * 100000.0));    //产生0-10000之间随机数
                bw.write(String.valueOf(randint));        //写入一个随机数
                bw.newLine();        //新的一行
            }
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }
}

