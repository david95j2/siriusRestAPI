package com.sierrabase.siriusapi.service.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.JsonModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Service
public class AnalysisCrackWorker {
    @Value("${path.repository.base}")
    private String repository_path;

    @Value("${path.worker.base}")
    private String worker_path;

    public boolean inferenceBySingleGpu(Integer albumId) {
        String pythonPath = URICreator.pathToString(worker_path,"AI_analyzer/temp_env/bin/python3");
        String scriptPath = URICreator.pathToString(worker_path,"AI_analyzer/analyzer.py");
        String sourcePath = URICreator.pathToString(repository_path,"album",String.valueOf(albumId), "origin");
        String targetPath = sourcePath.replace("origin","analysis");
        String[] scriptArguments = {"--img_dir", sourcePath, "--json_dir", targetPath};
        try {
            int resultCode = ExecuteScript.executeShellScript(pythonPath, null, scriptPath, scriptArguments);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Single GPU Inference error : "+e);
            return false;
        }
        return true;
    }

//    public boolean inferenceBySingleGpu(Integer albumId) {
//        String sourcePath = URICreator.pathToString(repository_path,"album",String.valueOf(albumId), "origin");
//
//        String pythonPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/venv_seg/bin/python3";
//
//        String scriptPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/inferences/inference_and_quantification_mmseg_single_part4.py";
//        String[] scriptArguments = {"--config", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/convnext_tiny_fpn_crack.py",
//                "--checkpoint", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/iter_32000.pth",
//                "--srx_dir", sourcePath,
//                "--srx_suffix", ".JPG"};
//        try {
//            int resultCode = ExecuteScript.executeShellScript(pythonPath, null, scriptPath, scriptArguments);
//            if (resultCode != 0) return false;
//        } catch (IOException | InterruptedException e) {
//            log.error("Single GPU Inference error : "+e);
//            return false;
//        }
//        return true;
//    }

    public boolean computeCrackToCameraDistancesForFolder(AlbumModel model) {
        String sourcePath = URICreator.pathToString(repository_path,"album",String.valueOf(model.getId()));
        String scriptPath = URICreator.pathToString(worker_path,"calc_dis_ver_2/build/calcDistance");
        String mapPath = URICreator.pathToString(repository_path,"facility",String.valueOf(model.getFacilityId()),
                "maps",String.valueOf(model.getFacilityMapId()),"GlobalMap.pcd");

        String[] scriptArguments = {sourcePath,mapPath};

        try {
            int resultCode = ExecuteScript.executeShellScript(null, null, scriptPath, scriptArguments);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Compute Crack To Camera Distances error : "+e);
            return false;
        }
        return true;
    }

    public boolean resetIndex(Integer albumId) {
        String sourcePath = URICreator.pathToString(repository_path,"album",String.valueOf(albumId),"analysis");
        String scriptPath = URICreator.pathToString(worker_path,"modify_crack_points.py");
        String[] scriptArguments = {sourcePath};

        try {
            int resultCode = ExecuteScript.executeShellScript("python3", null, scriptPath, scriptArguments);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Reset Index error : "+e);
            return false;
        }
        return true;
    }

    public boolean inferenceByMultiGpu(Integer albumId) {
        String sourcePath = URICreator.pathToString(repository_path,"album",String.valueOf(albumId), "origin");

        String pythonPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/venv_seg/bin/torchrun";
        String gpuNum = "--nproc_per_node=6";
        String scriptPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/inferences/inference_and_quantification_mmseg_torchrun.py";
        String[] scriptArguments = {"--config", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/convnext_tiny_fpn_crack.py",
                "--checkpoint", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/iter_32000.pth",
                "--srx_dir", sourcePath,
                "--srx_suffix", ".JPG"};
        try {
            int resultCode = ExecuteScript.executeShellScript(pythonPath, gpuNum, scriptPath, scriptArguments);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Multi GPU Inference error : "+e);
            return false;
        }
        return true;
    }

    public String getRepositoryPath() {
        return repository_path;
    }

    public boolean saveFile(String sourcePath, JsonModel updatedModel) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("update jsonFileName : "+sourcePath);
            objectMapper.writeValue(new File(sourcePath), updatedModel.getInfo());

            String pointsFileName = sourcePath.replace(".json", "_seg_points.json");
            objectMapper.writeValue(new File(pointsFileName), updatedModel.getPoints());
            log.info("Json Update Success !");
            return true;
        } catch (IOException e) {
            log.error("Json Update Error : "+e);
            return false;
        }
    }


    public String computeCrackToCameraDistanceForFile(Integer facilityId, Integer facilityMapId, AlbumPhotoModel albumPhotoModel) {
        String scriptPath = Paths.get(worker_path,"calc_dis_ver_2/build_single/calcDistance").toString();
        String imgPath = Paths.get(repository_path,albumPhotoModel.getAlbumPhotoPath().substring(albumPhotoModel.getAlbumPhotoPath().indexOf("/album"))).toString();
        String mapPath = Paths.get(repository_path,"facility", String.valueOf(facilityId),
                "maps", String.valueOf(facilityMapId), "GlobalMap.pcd").toString();
        String[] scriptArguments = {imgPath,mapPath};

        try {
            int resultCode = ExecuteScript.executeShellScript(null, null, scriptPath, scriptArguments);
            if (resultCode != 0) return null;
        } catch (IOException | InterruptedException e) {
            log.error("Compute Crack To Camera Distances modify error : "+e);
            return null;
        }
        return imgPath;
    }

    public boolean resetIndexForFile(String calDisResult) {

        return true;
    }
}
