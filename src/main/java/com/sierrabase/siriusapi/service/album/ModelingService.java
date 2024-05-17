package com.sierrabase.siriusapi.service.album;

import com.sierrabase.siriusapi.model.SourceInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class ModelingService {
    @Autowired
    private ModelingWorker modelingWorker;

    public boolean importModel(Integer albumId, SourceInfoModel model) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()-> {
            boolean importReuslt = modelingWorker.importModel(albumId,model.getUrl());
            if (!importReuslt) {
                log.error("Import model Error");
            }

            boolean createResult = modelingWorker.createGLTF(albumId);
            if (!createResult) {
                log.error("Can not create GLTF File");
            }
            log.info("import success");

        });
        executorService.shutdown(); // 스레드 풀 종료 시작

        return true;
    }
}
