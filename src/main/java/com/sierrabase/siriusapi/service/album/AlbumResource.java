package com.sierrabase.siriusapi.service.album;

import com.sierrabase.siriusapi.common.URICreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//@Builder
@Slf4j
@Data
@Service
public class AlbumResource {
    private String basePath;
    private Path targetPath;
    private ZipOutputStream zos;

    @Value("${path.repository.base}")
    private String repository_path;

    public void initialize(Integer albumId) {
        this.basePath = URICreator.pathToString(repository_path, "album", String.valueOf(albumId));
        this.targetPath = URICreator.pathTopath(basePath,albumId+".zip");

        try {
            this.zos = new ZipOutputStream(new FileOutputStream(targetPath.toFile()));
        } catch (FileNotFoundException e) {
            log.error("can not create zip file");
        }
    }

    public boolean includeImagesOn(Path path){
        try {
            Files.walk(path).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    String entryName = Paths.get(this.basePath).relativize(file).toString();
                    try {
                        this.zos.putNextEntry(new ZipEntry(entryName));
                        Files.copy(file,this.zos);
                        this.zos.closeEntry();
                    } catch (IOException e) {
                        log.error("can not add file");
                    }
                }
            });
        } catch (IOException e) {
            log.error("can not found file");
            return false;
        }
        return true;
    }


}
