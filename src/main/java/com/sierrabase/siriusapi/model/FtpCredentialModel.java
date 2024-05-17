package com.sierrabase.siriusapi.model;

import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.FacilityMapEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FtpCredentialModel {
    private String ftpIp;
    private String ftpPort;
    private String ftpId;
    private String ftpPassword;
    private String url;

    public FtpCredentialModel(FtpConfig ftpConfig, String filePath) {
        setFtpIp(ftpConfig.getLinuxFtpServer());
        setFtpPort(ftpConfig.getLinuxFtpPort());
        setFtpId(ftpConfig.getLinuxFtpUser());
        setFtpPassword(ftpConfig.getLinuxFtpPassword());
        setUrl(filePath);
    }
}
