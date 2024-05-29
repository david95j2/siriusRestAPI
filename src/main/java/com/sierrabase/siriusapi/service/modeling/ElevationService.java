package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;
import com.sierrabase.siriusapi.entity.analysis.AnalysisEntity;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityInfoModel;
import com.sierrabase.siriusapi.repository.analysis.AnalysisCrackEntityRepository;
import com.sierrabase.siriusapi.repository.analysis.AnalysisEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class ElevationService {

    @Autowired
    private ElevationWorker elevationWorker;
    @Autowired
    private AnalysisEntityRepository analysisEntityRepository;
    @Autowired
    private AnalysisCrackEntityRepository analysisCrackEntityRepository;
    @Autowired
    private FtpConfig ftpConfig;

    @Value("${path.repository.base}")
    private String repository_path;

    public Boolean createElevationFiles(Integer modelId, ThreeDimensionalFacilityInfoModel model) {
        String basePath = URICreator.pathToString(repository_path,"model",String.valueOf(modelId));

        // 저장할 폴더 생성
        String targetPath = URICreator.pathToString(basePath,"elevation",String.valueOf(model.getId()));
        File targetDirectory = new File(targetPath);
        boolean createFolderResult = false;
        if (!targetDirectory.exists())
            createFolderResult = targetDirectory.mkdirs();
        

        if (!createFolderResult) {
            log.error("Can not make Folder");
            return false;
        }
        
        Boolean createElevationResult = elevationWorker.createElevation(basePath,model);
        if (!createElevationResult) {
            log.error("Can not create Elevation");
            return false;
        }

        // 폴더 옮겨야 해
        Path sourcePath = Paths.get(targetPath);
        Path croppedImagPath = sourcePath.resolve("CMI");
        createDirectory(croppedImagPath);
        Path downsizedPath = sourcePath.resolve("downsized");
        createDirectory(downsizedPath);

        File sourceFolder = sourcePath.toFile();
        File[] files = sourceFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    try {
                        if (fileName.startsWith("CMI_") && fileName.endsWith(".png")) {
                            Files.move(file.toPath(), croppedImagPath.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                        } else if (fileName.startsWith("captured_donwsized") && fileName.endsWith(".png")) {
                            Files.move(file.toPath(), downsizedPath.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (IOException e) {
                        log.error("Error moving file :"+file.getName());
                    }
                }
            }
        }

        return true;
    }

    private static void createDirectory(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("Error Creating directory : "+path.toString());
            }
        }
    }

    public Boolean createAnalysisCrack(Integer modelId, Integer modelInfoId, AnalysisModel model) {

        String sourcePath = URICreator.pathToString(repository_path,"model",String.valueOf(modelId),"elevation",String.valueOf(modelInfoId));

        boolean result = elevationWorker.inferenceBySingleGpu(sourcePath);

        if (result) {
            log.info("완료");
            sourcePath = URICreator.pathToString(sourcePath,"analysis");
            File sourceFolder = Paths.get(sourcePath).toFile();
            File[] files = sourceFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!(file.getName().endsWith("_seg_points.json") || file.getName().endsWith("_skel.json"))) { // file.getName().matches(pattern)
                        try {
                            String jsonContent = new String(Files.readAllBytes(Paths.get(file.getPath())));
                            int crackCount;
                            if (jsonContent.length() != 5) {
                                crackCount = new JSONArray(jsonContent).length();
                            } else {
                                crackCount = 0;
                            }

                            AnalysisCrackModel analysisCrackModel = new AnalysisCrackModel(model.getAlbumId(), model.getId(),
                                    ftpConfig.getNginxUri()+file.getPath().substring(file.getPath().indexOf("/model")),crackCount);
                            AnalysisCrackEntity entity = new AnalysisCrackEntity(analysisCrackModel);
                            analysisCrackEntityRepository.save(entity);
                        } catch (IOException e) {
                            log.error("Can not read elevation json :"+file.getPath());
                        }
                    }
                }
            }
        }

        return result;
    }
}
