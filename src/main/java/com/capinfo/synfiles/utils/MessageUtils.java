package com.capinfo.synfiles.utils;

import java.util.LinkedList;
import java.util.Queue;

public class MessageUtils {
    //消息队列
    private static Queue<String> queue = new LinkedList<String>();

    public static void println(String message) {
        queue.offer(message);
    }
    //消息获取
    public static String put(){
        StringBuffer sb = new StringBuffer();
        while (!queue.isEmpty()){
            sb.append(queue.poll()+"<br/>");
        }
        return sb.toString();
    }
}
