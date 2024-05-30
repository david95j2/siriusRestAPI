package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;
import com.sierrabase.siriusapi.entity.analysis.AnalysisEntity;
import com.sierrabase.siriusapi.entity.modeling.NetworkOfCrackEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelEntity;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.model.modeling.NetworkOfCrackModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityInfoModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModel;
import com.sierrabase.siriusapi.repository.analysis.AnalysisCrackEntityRepository;
import com.sierrabase.siriusapi.repository.analysis.AnalysisEntityRepository;
import com.sierrabase.siriusapi.repository.modeling.NetworkOfCrackRepository;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalModelRepository;
import com.sierrabase.siriusapi.service.analysis.AnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class ElevationService {

    @Autowired
    private ElevationWorker elevationWorker;
    @Autowired
    private AnalysisEntityRepository analysisEntityRepository;
    @Autowired
    private ThreeDimensionalModelRepository threeDimensionalModelRepository;
    @Autowired
    private AnalysisCrackEntityRepository analysisCrackEntityRepository;
    @Autowired
    private NetworkOfCrackRepository networkOfCrackRepository;
    @Autowired
    private ThreeDimensionalModelService threeDimensionalModelService;
    @Autowired
    private AnalysisService analysisService;
    @Autowired
    private FtpConfig ftpConfig;

    @Value("${path.repository.base}")
    private String repository_path;

    public Boolean createElevationFiles(Integer modelId, ThreeDimensionalFacilityInfoModel model) {
        String basePath = URICreator.pathToString(repository_path, "model", String.valueOf(modelId));

        // 저장할 폴더 생성
        String targetPath = URICreator.pathToString(basePath, "elevation", String.valueOf(model.getId()));
        File targetDirectory = new File(targetPath);
        boolean createFolderResult = false;
        if (!targetDirectory.exists())
            createFolderResult = targetDirectory.mkdirs();


        if (!createFolderResult) {
            log.error("Can not make Folder");
            return false;
        }

        Boolean createElevationResult = elevationWorker.createElevation(basePath, model);
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
                        log.error("Error moving file :" + file.getName());
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
                log.error("Error Creating directory : " + path.toString());
            }
        }
    }

    public void createAnalysisCrack(Integer modelId, Integer modelInfoId, AnalysisModel model, ThreeDimensionalModel threeDimensionalModel) {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
        String sourcePath = URICreator.pathToString(repository_path, "model", String.valueOf(modelId), "elevation", String.valueOf(modelInfoId));
//        String sourcePath = URICreator.pathToString(repository_path, "model", String.valueOf(modelId), "elevation", String.valueOf(modelInfoId));
        log.info("service sourcePath : " + sourcePath);

        boolean result = elevationWorker.inferenceBySingleGpu(sourcePath);
        log.info("service result : " + result);
        String zipFilePath = URICreator.pathToString(repository_path,"model",String.valueOf(modelId),"elevation",modelId+".zip");
        log.info("zipFilePath : "+zipFilePath);
        boolean compressedResult = zipDirectory(sourcePath, zipFilePath);
        if (compressedResult) {
            String zipPath = zipFilePath;
            AnalysisCrackModel analysisCrackModel = new AnalysisCrackModel(model.getAlbumId(), model.getId(),
                    ftpConfig.getNginxUri() + zipPath.substring(zipPath.indexOf("/model")), 0);
            AnalysisCrackEntity entity = new AnalysisCrackEntity(analysisCrackModel);
            analysisCrackEntityRepository.save(entity);
        }

        if (result) {
            log.info("완료");
            sourcePath = URICreator.pathToString(sourcePath, "analysis");
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
                                    ftpConfig.getNginxUri() + file.getPath().substring(file.getPath().indexOf("/model")), crackCount);
                            AnalysisCrackEntity entity = new AnalysisCrackEntity(analysisCrackModel);
                            analysisCrackEntityRepository.save(entity);
                        } catch (IOException e) {
                            log.error("Can not read elevation json :" + file.getPath());
                        }
                    }
                }
            }
            model.setStatus("Completed");
            AnalysisEntity entity = new AnalysisEntity(model);
            analysisEntityRepository.save(entity);
            threeDimensionalModel.setElevationStatus("Completed");
            ThreeDimensionalModelEntity threeDimensionalModelEntity = new ThreeDimensionalModelEntity(threeDimensionalModel);
            threeDimensionalModelRepository.save(threeDimensionalModelEntity);

            NetworkOfCrackModel networkOfCrackModel = new NetworkOfCrackModel();
            networkOfCrackModel.setThreeDimensionalModelId(threeDimensionalModel.getId());
            networkOfCrackModel.setStatus("Waiting");
            networkOfCrackModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
            NetworkOfCrackEntity networkOfCrackEntity = new NetworkOfCrackEntity(networkOfCrackModel);
            NetworkOfCrackEntity createdNetworkOfCrack = networkOfCrackRepository.save(networkOfCrackEntity);
            log.info("Created 균열망도 :" + createdNetworkOfCrack);
        } else {
            log.error("Error elevation inference");
            model.setStatus("Error");
            AnalysisEntity entity = new AnalysisEntity(model);
            analysisEntityRepository.save(entity);
            threeDimensionalModel.setElevationStatus("Error");
            ThreeDimensionalModelEntity threeDimensionalModelEntity = new ThreeDimensionalModelEntity(threeDimensionalModel);
            threeDimensionalModelRepository.save(threeDimensionalModelEntity);
        }
//        });
    }

    public static boolean zipDirectory(String sourceDirPath, String zipFilePath) {
        Path sourceDir = Paths.get(sourceDirPath);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            File sourceFile = sourceDir.toFile();
            compressDirectoryToZipfile(sourceFile, sourceFile.getName(), zipOut);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static boolean compressDirectoryToZipfile(File fileToCompress, String filename, ZipOutputStream zipOut) {
        if (fileToCompress.isDirectory()) {
            File[] children = fileToCompress.listFiles();
            for (File childFile : children) {
                compressDirectoryToZipfile(childFile, filename + "/" + childFile.getName(), zipOut);
            }
        } else {
            try (FileInputStream fis = new FileInputStream(fileToCompress)) {
                ZipEntry zipEntry = new ZipEntry(filename);
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                zipOut.closeEntry();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }


//    public boolean createElevation(Integer albumId, Integer modelId, ThreeDimensionalFacilityInfoModel model) {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
//            ThreeDimensionalModel threeDimensionalModel = threeDimensionalModelService.getEntityById(modelId);
//            threeDimensionalModel.setElevationStatus("Running");
//            threeDimensionalModelService.updateEntity(threeDimensionalModel.getId(), threeDimensionalModel);
//
//            Boolean result = createElevationFiles(modelId, model);
//            if (!result) {
//                log.error("Error create elevation");
//                threeDimensionalModel.setElevationStatus("Error");
//                threeDimensionalModelService.updateEntity(threeDimensionalModel.getId(), threeDimensionalModel);
//                return;
//            }
//
//            AnalysisModel analysisModel = new AnalysisModel();
//            analysisModel.setAlbumId(albumId);
//            analysisModel.setName("elevation crack");
//            analysisModel.setType(1);
//            analysisModel.setTypeName("Segmentation");
//            analysisModel.setStatus("Running");
//            analysisModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
//            AnalysisModel createdAnalysisModel = analysisService.createEntity(analysisModel);
//
//            log.info("Elevation analysis :" + createdAnalysisModel);
//            createAnalysisCrack(modelId, model.getId(), createdAnalysisModel, threeDimensionalModel);
//        });
//        return true;
//    }

    @Async("taskExecutor")
    public void createElevation(Integer albumId, Integer modelId, ThreeDimensionalFacilityInfoModel model) {
        try {
            ThreeDimensionalModel threeDimensionalModel = threeDimensionalModelService.getEntityById(modelId);
            threeDimensionalModel.setElevationStatus("Running");
            threeDimensionalModelService.updateEntity(threeDimensionalModel.getId(), threeDimensionalModel);

            Boolean result = createElevationFiles(modelId, model);
            if (!result) {
                log.error("Error creating elevation");
                threeDimensionalModel.setElevationStatus("Error");
                threeDimensionalModelService.updateEntity(threeDimensionalModel.getId(), threeDimensionalModel);
                return;
            }

            AnalysisModel analysisModel = new AnalysisModel();
            analysisModel.setAlbumId(albumId);
            analysisModel.setName("elevation crack");
            analysisModel.setType(1);
            analysisModel.setTypeName("Segmentation");
            analysisModel.setStatus("Running");
            analysisModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
            AnalysisModel createdAnalysisModel = analysisService.createEntity(analysisModel);

            log.info("Elevation analysis created: " + createdAnalysisModel);
            createAnalysisCrack(modelId, model.getId(), createdAnalysisModel, threeDimensionalModel);
        } catch (Exception e) {
            log.error("Exception in createElevation: ", e);
        }
    }
}
