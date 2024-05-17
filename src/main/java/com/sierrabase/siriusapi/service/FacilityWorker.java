package com.sierrabase.siriusapi.service;

import com.sierrabase.siriusapi.common.URICreator;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Slf4j
@Service
public class FacilityWorker {
    @Value("${path.repository.base}")
    private String repository_path;
    public boolean uploadFile(MultipartFile file, Integer facilityId) {
        String basePath = URICreator.pathToString(Paths.get(repository_path,"facility",
                String.valueOf(facilityId), "thumbnails").toString());

        try (InputStream in = file.getInputStream()) {
            File directory = new File(basePath);
            if(!directory.exists())
                directory.mkdirs();

            String fileName = file.getOriginalFilename().replaceAll(" ", "");
            File destinationFile = new File(basePath, fileName);
            Thumbnails.of(in)
                    .size(320, 200)
                    .toFile(destinationFile);
            return true;
        } catch (IOException e) {
            log.error("Facility Thumbnails Upload Error : "+ e);
            return false;
        }
    }
}
