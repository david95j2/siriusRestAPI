package com.sierrabase.siriusapi.service.analysis;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.FacilityMapEntity;
import com.sierrabase.siriusapi.entity.album.AlbumEntity;
import com.sierrabase.siriusapi.entity.analysis.AnalysisElevationEntity;
import com.sierrabase.siriusapi.entity.analysis.AnalysisEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import com.sierrabase.siriusapi.entity.modeling.inspectionNetworkMapEntity;
import com.sierrabase.siriusapi.model.FacilityMapModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisElevationModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.model.modeling.InspectionNetworkMapModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityRoiModel;
import com.sierrabase.siriusapi.repository.analysis.AnalysisElevationEntityRepository;
import com.sierrabase.siriusapi.repository.analysis.AnalysisEntityRepository;
import com.sierrabase.siriusapi.repository.modeling.InspectionNetworkMapEntityRepository;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalFacilityRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class AnalysisElevationService {

    @Autowired
    private AnalysisElevationWorker analysisElevationWorker;
    @Autowired
    private AnalysisEntityRepository analysisEntityRepository;
    @Autowired
    private AnalysisElevationEntityRepository analysisElevationEntityRepository;
    @Autowired
    private InspectionNetworkMapEntityRepository inspectionNetworkMapEntityRepository;
    @Autowired
    private ThreeDimensionalFacilityRepository threeDimensionalFacilityRepository;

    @Autowired
    private AnalysisService analysisService;
    @Autowired
    private FtpConfig ftpConfig;

    @Value("${path.repository.base}")
    private String repository_path;

    public ArrayList<AnalysisElevationModel> getEntitiesByRoiId(Integer roiId) {
        List<AnalysisElevationEntity> entities = analysisElevationEntityRepository.findAllByRoiId(roiId);

        if (entities.size() <= 0)
            return null;

        ArrayList<AnalysisElevationModel> modelList = new ArrayList<AnalysisElevationModel>();
        for (AnalysisElevationEntity entity : entities) {
            modelList.add(new AnalysisElevationModel(entity));
        }

        return modelList;
    }

    public AnalysisElevationModel getEntityById(Integer id) {
        Optional<AnalysisElevationEntity> entity = analysisElevationEntityRepository.findById(id);

        log.debug("entity", entity);

        if (!entity.isPresent())
            return null;

        AnalysisElevationModel model = new AnalysisElevationModel(entity.get());

        return model;
    }

    public AnalysisElevationModel createEntity(AnalysisElevationModel model) {
        AnalysisElevationEntity entity = new AnalysisElevationEntity(model);
        // Set properties from analysisElevation to entity
        entity = analysisElevationEntityRepository.save(entity);
        return new AnalysisElevationModel(entity);
    }

    public AnalysisElevationModel updateEntity(Integer id, AnalysisElevationModel model) {
        Optional<AnalysisElevationEntity> optionalEntity = analysisElevationEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            AnalysisElevationEntity entity = optionalEntity.get();
            // Update properties from analysisElevation to entity
            entity = analysisElevationEntityRepository.save(new AnalysisElevationEntity(entity.getAnalysis_elevation_id(), model));
            return new AnalysisElevationModel(entity);
        } else {
            log.error("Analysis elevation not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<AnalysisElevationEntity> optionalEntity = analysisElevationEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            analysisElevationEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("Analysis elevation not found with id: {}", id);
            return false;
        }
    }

    public Boolean createElevationFiles(Integer modelId, ThreeDimensionalFacilityRoiModel model) {
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

        Boolean createElevationResult = analysisElevationWorker.createElevation(basePath, model);
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

    public void createAnalysisCrack(Integer modelId, Integer roiId, AnalysisModel analysisModel, ThreeDimensionalFacilityModel model) {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
        String sourcePath = URICreator.pathToString(repository_path, "model", String.valueOf(modelId), "elevation", String.valueOf(roiId));
//        String sourcePath = URICreator.pathToString(repository_path, "model", String.valueOf(modelId), "elevation", String.valueOf(modelInfoId));
        log.info("service sourcePath : " + sourcePath);

        boolean result = analysisElevationWorker.inferenceBySingleGpu(sourcePath);
        log.info("service result : " + result);
        String zipFilePath = URICreator.pathToString(repository_path,"model",String.valueOf(modelId),"elevation",modelId+".zip");
        log.info("zipFilePath : "+zipFilePath);
        boolean compressedResult = zipDirectory(sourcePath, zipFilePath);
        log.info("compressed Result : "+compressedResult);

        if (result) {
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

                            AnalysisElevationModel analysisElevationModel = new AnalysisElevationModel(analysisModel.getId(), roiId,
                                    ftpConfig.getNginxUri() + file.getPath().substring(file.getPath().indexOf("/model")), crackCount);
                            AnalysisElevationEntity entity = new AnalysisElevationEntity(analysisElevationModel);
                            analysisElevationEntityRepository.save(entity);
                        } catch (IOException e) {
                            log.error("Can not read elevation json :" + file.getPath());
                        }
                    }
                }
            }
            analysisModel.setStatus("Completed");
            AnalysisEntity entity = new AnalysisEntity(analysisModel);
            analysisEntityRepository.save(entity);
            model.setElevationStatus("Completed");
            ThreeDimensionalFacilityEntity threeDimensionalFacilityEntity = new ThreeDimensionalFacilityEntity(model);
            threeDimensionalFacilityRepository.save(threeDimensionalFacilityEntity);

            InspectionNetworkMapModel inspectionNetworkMapModel = new InspectionNetworkMapModel();
            inspectionNetworkMapModel.setStatus("Waiting");
            inspectionNetworkMapModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
            inspectionNetworkMapEntity inspectionNetworkMapEntity = new inspectionNetworkMapEntity(inspectionNetworkMapModel);
            inspectionNetworkMapEntity createdNetworkOfCrack = inspectionNetworkMapEntityRepository.save(inspectionNetworkMapEntity);
            log.info("Created 균열망도 :" + createdNetworkOfCrack);
        } else {
            log.error("Error elevation inference");
            analysisModel.setStatus("Error");
            AnalysisEntity entity = new AnalysisEntity(analysisModel);
            analysisEntityRepository.save(entity);
            model.setElevationStatus("Error");
            ThreeDimensionalFacilityEntity threeDimensionalFacilityEntity = new ThreeDimensionalFacilityEntity(model);
            threeDimensionalFacilityRepository.save(threeDimensionalFacilityEntity);
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


    @Async("taskExecutor")
    public void createElevation(Integer modelId, ThreeDimensionalFacilityRoiModel model) {
        try {
            Optional<ThreeDimensionalFacilityEntity> tdfEntity = threeDimensionalFacilityRepository.findById(modelId);
            if(!tdfEntity.isPresent()) {
                log.error("3D Facility Data Not Found in CreateElevation Service: "+modelId);
                return;
            }
            Optional<AlbumEntity> albumEntity = threeDimensionalFacilityRepository.findByAlbumDateTime(tdfEntity.get().getAlbum_datetime());
            if(!albumEntity.isPresent()) {
                log.error("Album Data Not Found in CreateElevation Service: "+tdfEntity.get().getAlbum_datetime());
                return;
            }

            ThreeDimensionalFacilityModel tdfModel = new ThreeDimensionalFacilityModel(tdfEntity.get());
            tdfModel.setElevationStatus("Running");
            threeDimensionalFacilityRepository.save(new ThreeDimensionalFacilityEntity(model.getThreeDimensionalFacilityId(), tdfModel));

            Boolean result = createElevationFiles(modelId, model);
            if (!result) {
                log.error("Error creating elevation");
                tdfModel.setElevationStatus("Error");
                threeDimensionalFacilityRepository.save(new ThreeDimensionalFacilityEntity(model.getThreeDimensionalFacilityId(), tdfModel));
                return;
            }

            AnalysisModel analysisModel = new AnalysisModel();
            analysisModel.setAlbumId(albumEntity.get().getAlbum_id());
            analysisModel.setName("elevation crack");
            analysisModel.setType(1);
            analysisModel.setTypeName("Segmentation");
            analysisModel.setStatus("Running");
            analysisModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
            AnalysisModel createdAnalysisModel = analysisService.createEntity(analysisModel);

            log.info("Elevation analysis created: " + createdAnalysisModel);
            createAnalysisCrack(modelId, model.getId(), createdAnalysisModel, tdfModel);
        } catch (Exception e) {
            log.error("Exception in createElevation: ", e);
        }
    }
}
