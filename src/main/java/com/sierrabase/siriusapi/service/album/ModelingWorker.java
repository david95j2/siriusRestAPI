package com.sierrabase.siriusapi.service.album;

import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ModelingWorker {
    @Value("${path.repository.base}")
    private String repository_path;

    @Value("${path.worker.base}")
    private String worker_path;

    @Autowired
    private FtpConfig ftpConfig;

    public boolean importModel(Integer albumId, String resourcePath) {
        String scriptPath = URICreator.pathToString(worker_path,"ftpClient.py");
        String targetPath = URICreator.pathToString(repository_path,"album", String.valueOf(albumId),"3D_modeling");
        String[] runArgument = {ftpConfig.getWindowFtpServer(), ftpConfig.getWindowFtpPort(), ftpConfig.getWindowFtpUser(), ftpConfig.getWindowFtpPassword()
                , "download",targetPath, resourcePath};

        try {
            int resultCode = ExecuteScript.executeShellScript("python3", null, scriptPath, runArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("ModelingWorker Import Error : " + e);
            return false;
        }

        return true;
    }

    public boolean createGLTF(Integer albumId) {
        String targetPath = URICreator.pathToString(repository_path,"album", String.valueOf(albumId),"3D_modeling");

        String gltfRunPath = URICreator.pathToString(worker_path,"convert2glb.py");
        String[] gltfArgument = {"--",targetPath+"/Data","Tile",targetPath,"16"};

        try {
            int resultCode = ExecuteScript.executeShellScript("blender", "-b -P", gltfRunPath, gltfArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Blender error : "+e);
            return false;
        }
        return true;
    }
}
