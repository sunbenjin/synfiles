package com.capinfo.synfiles.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FtpConfigInfo {
    private String user;
    private String password;
    private String server;
    private String location;
    private String fileName;
    private long maxWorkTime;
    private int port;
    private String encoding;
}
