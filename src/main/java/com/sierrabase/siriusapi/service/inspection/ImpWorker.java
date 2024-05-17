package com.sierrabase.siriusapi.service.inspection;

import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.model.inspection.FitAreaProgramModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Service
public class ImpWorker {
    @Value("${path.repository.base}")
    private String repository_path;
    @Value("${path.worker.base}")
    private String worker_path;


    public boolean createPathPlanning(Integer facilityId, Integer facilityMapId, FitAreaProgramModel model) {
        String sourcePath = URICreator.pathToString(repository_path,"facility", String.valueOf(facilityId), "maps", String.valueOf(facilityMapId), "GlobalMap.pcd");
        String scriptPath = URICreator.pathToString(worker_path,"pcd-manager3/build/fit_area");
        String[] scriptArgument = {sourcePath, model.getPort(), model.getUserId()};

        try {
            int resultCode = ExecuteScript.executeShellScript(null, null, scriptPath, scriptArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("fit_area error : "+e);
            return false;
        }

        return true;
    }
}
