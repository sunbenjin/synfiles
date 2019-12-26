package com.capinfo.synfiles.controller;

import com.capinfo.synfiles.ftpserver.FtpClientUtils;
import com.capinfo.synfiles.ftpserver.FtpServer;
import com.capinfo.synfiles.utils.MessageUtils;
import com.capinfo.synfiles.webserver.SimpleMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

@Controller
public class MesServer {



    @Autowired
    private SimpleMessageService simpleMessageService;

    @Scheduled(fixedRate = 1000)
    public void information() {
        String message = MessageUtils.put();
        if (!StringUtils.isEmpty(message)) {
            simpleMessageService.sendTopicMessage("/topic/callback",message);
        }
    }


    @Scheduled(fixedRate = 1000)
    public void monitorFtpServer() {
        FtpServer server = FtpServer.getServer();
        if (server!=null){
            //System.out.println(server.monitor()?"关闭":"开启");
            simpleMessageService.sendTopicMessage("/topic/ftpserver",server.monitor()?"关闭":"开启");
        }else{
            //System.out.println("关闭");
            FtpClientUtils client = FtpClientUtils.getClient();
            if (client==null){
                simpleMessageService.sendTopicMessage("/topic/ftpserver","关闭");
                return;
            }
            if("localhost".equals(FtpClientUtils.getFtpAddress())){
                simpleMessageService.sendTopicMessage("/topic/ftpserver","关闭");
            }else{
               // if(client.isConnect()){
             if (client.loginSuccess){
                    simpleMessageService.sendTopicMessage("/topic/ftpserver","连接成功");
                }else{
                    simpleMessageService.sendTopicMessage("/topic/ftpserver","未连接");
                }
            }
        }
    }
}
