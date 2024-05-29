package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ElevationWorker {

    @Value("${path.worker.base}")
    private String worker_path;

    public Boolean createElevation(String basePath, ThreeDimensionalFacilityInfoModel model) {
        String scriptPath = URICreator.pathToString(worker_path,"modelCapture/build/GLBLoader");
        String sourcePath = URICreator.pathToString(basePath,"original_model.glb");
        String targetPath = URICreator.pathToString(basePath,"elevation", String.valueOf(model.getId()));
        String[] runArgument = {sourcePath, targetPath,
                                model.minWidthToString(),model.minHeightToString(),model.minDepthToString(),
                                model.maxWidthToString(),model.maxHeightToString(),model.maxDepthToString(),
                                model.rotationToString(),model.verticalToString()};

        try {
            int resultCode = ExecuteScript.executeShellScript(null,null,scriptPath,runArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("3D Model Capture Error : "+e);
            return false;
        }
        return true;
    }

    public boolean inferenceBySingleGpu(String basePath) {
        String pythonPath = URICreator.pathToString(worker_path,"AI_analyzer/temp_env/bin/python3");
        String scriptPath = URICreator.pathToString(worker_path,"AI_analyzer/analyzer.py");
        String sourcePath = URICreator.pathToString(basePath, "CMI");
        String targetPath = sourcePath.replace("CMI","analysis");
        String[] scriptArguments = {"--img_dir", sourcePath, "--json_dir", targetPath};
        try {
            int resultCode = ExecuteScript.executeShellScript(pythonPath, null, scriptPath, scriptArguments);
            if (resultCode != 0) return false;
            log.info("Elevation inference ResultCode : "+resultCode);
        } catch (IOException | InterruptedException e) {
            log.error("Single GPU Inference error : "+e);
            return false;
        }
        return true;
    }
}
