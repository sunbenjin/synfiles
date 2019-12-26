package com.capinfo.synfiles.ftpserver;

import com.capinfo.synfiles.controller.ServerController;
import com.capinfo.synfiles.listener.FileListener;
import com.capinfo.synfiles.utils.MessageUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FileDirMonitorServer {

    private static Logger LOGGER = LoggerFactory.getLogger(FileDirMonitorServer.class);

    private static FileDirMonitorServer server = null;

    private static Map<String,FileAlterationMonitor> monitors = new HashMap<String,FileAlterationMonitor>();


    public static FileDirMonitorServer newInstance(Set<String> rootDirs){
        if(server==null){
            LOGGER.info("创建:FileDirMonitorServer对象");
            server = new FileDirMonitorServer(rootDirs,1);
        }
        return server;
    }

    private FileDirMonitorServer(Set<String> rootDirs, int duration) {

        Iterator iter = rootDirs.iterator();
        while (iter.hasNext()){
            String rootDir = (String)iter.next();
            LOGGER.info("监控路径:"+rootDir);
            long interval = TimeUnit.SECONDS.toMillis(duration);
            // 创建过滤器
            IOFileFilter directories = FileFilterUtils.and(
                    FileFilterUtils.directoryFileFilter(),
                    HiddenFileFilter.VISIBLE);
            IOFileFilter files  = FileFilterUtils.and(
                    FileFilterUtils.fileFileFilter());
            IOFileFilter filter = FileFilterUtils.or(directories, files);
            // 使用过滤器
            //FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir), filter);
            //不使用过滤器
            FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
            observer.addListener(new FileListener(rootDir));
            //创建文件变化监听器
            monitors.put(rootDir,new FileAlterationMonitor(interval, observer));
        }
    }

    public static FileDirMonitorServer getServer() {
        return server;
    }

    public void start() {
        // 开始监控
        try {
            if (monitors!=null){
                Set<Map.Entry<String, FileAlterationMonitor>> entries = monitors.entrySet();
                for (Map.Entry<String, FileAlterationMonitor> monitor : entries) {
                    monitor.getValue().start();
                    MessageUtils.println("开启文件夹\""+monitor.getKey()+"\"的监控");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopAll() {
        try {
            if (monitors!=null){
                Set<Map.Entry<String, FileAlterationMonitor>> entries = monitors.entrySet();
                for (Map.Entry<String, FileAlterationMonitor> monitor : entries) {
                    monitor.getValue().stop();
                    MessageUtils.println("关闭\""+monitor.getKey()+"\"的监控");
                }
                //清理server 重新设置
                server = null;
                //清理监控器
                monitors = new HashMap<String,FileAlterationMonitor>();
            }
        } catch (Exception e) {

            //e.printStackTrace();
        }
    }

}
