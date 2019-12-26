package com.capinfo.synfiles.webserver;


import com.capinfo.synfiles.utils.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SimpleMessageService {

    @Autowired
    private SimpMessagingTemplate template;

    public void sendTopicMessage(String url,String message){
        template.convertAndSend(url,new SendMessage(message));
    }

}
