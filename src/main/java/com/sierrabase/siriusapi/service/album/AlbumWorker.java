package com.sierrabase.siriusapi.service.album;

import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class AlbumWorker {
    @Value("${path.repository.base}")
    private String repository_path;

    @Value("${path.worker.base}")
    private String worker_path;

    @Autowired
    private FtpConfig ftpConfig;

    public boolean importAlbum(Integer albumId, String resourcePath) {
        String scriptPath = URICreator.pathToString(worker_path,"ftpClient.py");
        String targetPath = URICreator.pathToString(repository_path,"album", String.valueOf(albumId));
        String[] runArgument = {ftpConfig.getWindowFtpServer(), ftpConfig.getWindowFtpPort(), ftpConfig.getWindowFtpUser(), ftpConfig.getWindowFtpPassword()
        , "download",targetPath, resourcePath};

        try {
            int resultCode = ExecuteScript.executeShellScript("python3", null, scriptPath, runArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("AlbumWorker Import Error : " + e);
            return false;
        }

        return true;
    }

    public boolean extractAlbum(Integer albumId, String resourcePath) {
        File basePath = new File(URICreator.pathToString(repository_path,"album",String.valueOf(albumId)));
        String targetPath = URICreator.pathToString(repository_path,resourcePath);
        String[] runArgument = {targetPath,basePath.getPath()};

        try {
            int resultCode = ExecuteScript.executeShellScript(null,null,"sb-extract",runArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("AlbumWorker Extract Erorr : "+e);
            return false;
        }
        return true;
    }

    public boolean alignExtractedAlbum(Integer albumId, SourceInfoModel model) {
        String basePath = URICreator.pathToString(repository_path,"album",String.valueOf(albumId));
        File[] topFiles = new File(basePath).listFiles();

        if (topFiles == null | topFiles == null) {
            log.error("AlbumWorker Error : No files found in the directory.");
            return false;
        }

        if (model.isTopFolder()) {
            String topFilePath = topFiles[0].getPath();
            Path oldDirPath = URICreator.pathTopath(topFilePath,"origin");
            Path newDirPath = URICreator.pathTopath(basePath,"origin");

            try {
                Files.move(oldDirPath,newDirPath, StandardCopyOption.REPLACE_EXISTING);

                if (model.isExistAnalysis()) {
                    oldDirPath = URICreator.pathTopath(topFilePath,"analysis");
                    newDirPath = URICreator.pathTopath(basePath,"analysis");

                    Files.move(oldDirPath, newDirPath ,StandardCopyOption.REPLACE_EXISTING);
                }

                boolean deleteDir = new File(topFilePath).delete();
                if (!deleteDir) {
                    log.warn("AlbumWorker warning : can not delete a folder");
                }
            } catch (IOException e ) {
                log.error("AlbumWorker Error relocating directories : "+e.getMessage());
            }
        }
        return true;
    }

    public boolean resizePictures(String basePath, String saveType, String size) {
        try {
            String[] thumbnailsArgument = {basePath + "/origin", basePath + "/"+saveType, size};
            int resultCode = ExecuteScript.executeShellScript(null, null, "sb-img-resizer", thumbnailsArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("thumbnails resize error : "+e);
            return false;
        }
        return true;
    }
}
