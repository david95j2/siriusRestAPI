package com.sierrabase.siriusapi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Getter
@Configuration
public class FtpConfig {
    @Value("${linux.ftp.server}")
    private String linuxFtpServer;

    @Value("${linux.ftp.port}")
    private String linuxFtpPort;

    @Value("${linux.ftp.user}")
    private String linuxFtpUser;

    @Value("${linux.ftp.user.password}")
    private String linuxFtpPassword;

    @Value("${linux.ftp.nginx}")
    private String linuxNginxPort;

    @Value("${window.ftp.server}")
    private String windowFtpServer;

    @Value("${window.ftp.port}")
    private String windowFtpPort;

    @Value("${window.ftp.user}")
    private String windowFtpUser;

    @Value("${window.ftp.user.password}")
    private String windowFtpPassword;

    public String getNginxUri(){
        return "http://"+linuxFtpServer+":"+linuxNginxPort+"/repo";
    }

}
