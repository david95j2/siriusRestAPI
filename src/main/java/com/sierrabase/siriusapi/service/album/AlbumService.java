package com.sierrabase.siriusapi.service.album;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.album.AlbumEntity;

import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoPosModel;
import com.sierrabase.siriusapi.model.album.CameraModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.repository.album.AlbumEntityRepository;
import com.sierrabase.siriusapi.service.analysis.AnalysisCrackService;
import com.sierrabase.siriusapi.service.analysis.AnalysisService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@Service
public class AlbumService {
    @Autowired
    private FtpConfig ftpConfig;
    @Autowired
    private AlbumEntityRepository albumEntityRepository;
    @Autowired
    CameraService cameraService;
    @Autowired
    AlbumPhotoService albumPhotoService;
    @Autowired
    AlbumPhotoPosService albumPhotoPosService;
    @Autowired
    AnalysisService analysisService;
    @Autowired
    AnalysisCrackService analysisCrackService;
    @Autowired
    AlbumWorker albumWorker;
    @Autowired
    AlbumResource albumResource;

    @Value("${path.repository.base}")
    private String repository_path;

    public ArrayList<AlbumModel> getByConditions(Optional<Integer> facilityId, Optional<Integer> facilityMapId) {
        List<AlbumEntity> entities;
        if (facilityId.isPresent() && facilityMapId.isPresent()) {
            entities =  albumEntityRepository.findAllByFacilityIdAndFacilityMapId(facilityId.get(), facilityMapId.get());
        } else if (facilityId.isPresent()) {
            entities =  albumEntityRepository.findAllByFacilityId(facilityId.get());
        } else if (facilityMapId.isPresent()) {
            entities =  albumEntityRepository.findAllByFacilityMapId(facilityMapId.get());
        } else {
            entities =  albumEntityRepository.findAll();
        }
        ArrayList<AlbumModel> modelList = new ArrayList<AlbumModel>();
        for (AlbumEntity entity : entities) {
            modelList.add(new AlbumModel(entity));
        }

        return modelList;
    }

    public AlbumModel getEntityById(Integer id) {
        Optional<AlbumEntity> entity = albumEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        AlbumModel model = new AlbumModel(entity.get());

        return model;
    }

    public AlbumModel createEntity(AlbumModel albumModel) {
        AlbumEntity entity = new AlbumEntity(albumModel);
        // Set properties from albumModel to entity
        entity = albumEntityRepository.save(entity);
        return new AlbumModel(entity);
    }

    public AlbumModel updateEntity(Integer id, AlbumModel albumModel) {
        Optional<AlbumEntity> optionalEntity = albumEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            AlbumEntity entity = optionalEntity.get();
            // Update properties from albumModel to entity

            entity = albumEntityRepository.save(new AlbumEntity(entity.getAlbum_id(), albumModel));
            return new AlbumModel(entity);
        } else {
            log.error("Album not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<AlbumEntity> optionalEntity = albumEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            albumEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("Album not found with id: {}", id);
            return false;
        }
    }

    // save compressed file
    public void saveCompressedFile(Integer id, MultipartFile file) {
        String directoryPath = URICreator.pathToString(repository_path,"album",String.valueOf(id));

        File archive = new File(directoryPath+"/"+file.getOriginalFilename());

        if (!archive.getParentFile().exists()) {
            boolean createdFolder = archive.getParentFile().mkdirs();
            if (!createdFolder)
                log.error("Failed to create Album Directory : " + archive.getPath());
        } else {
            log.info("Saved to Zip : " +archive.getPath());
        }

        try {
            file.transferTo(archive);
        } catch (IOException e) {
            log.error("Failed to save Zip : "+ e);
        }
    }

    public Map<String, String> extractMetadataFromImage(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);

        Map<String, String> extractedInfo = new HashMap<>();

        // 기본 이미지 정보
        ExifIFD0Directory ifd0Dir = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (ifd0Dir != null) {
            extractedInfo.put("Make", ifd0Dir.getString(ExifIFD0Directory.TAG_MAKE));
            extractedInfo.put("Model", ifd0Dir.getString(ExifIFD0Directory.TAG_MODEL));
        }

        // 사진 촬영 정보
        ExifSubIFDDirectory subIfdDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (subIfdDirectory != null) {
            extractedInfo.put("Image Width", subIfdDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
            extractedInfo.put("Image Height", subIfdDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
            extractedInfo.put("Focal Length", subIfdDirectory.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
            extractedInfo.put("F-Number", subIfdDirectory.getString(ExifSubIFDDirectory.TAG_FNUMBER));
            extractedInfo.put("Exposure Bias Value", subIfdDirectory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_BIAS));
            extractedInfo.put("Exposure Time", subIfdDirectory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
            extractedInfo.put("ISO Speed Ratings", subIfdDirectory.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
        }

        // 추출한 정보 출력
        return extractedInfo;
    }


    public boolean importAlbum(Integer albumId, SourceInfoModel sourceInfo) {
        String sourcePath = sourceInfo.getUrl();

        boolean importResult = albumWorker.importAlbum(albumId, sourcePath);
        if (!importResult) {
            return false;
        }

        boolean extractResult = albumWorker.extractAlbum(albumId, sourcePath);
        if (!extractResult) {
            return false;
        }

//        boolean alignResult = albumWorker.alignExtractedAlbum(albumId, sourceInfo);
//        if (!alignResult) {
//            return false;
//        }

        // initialize variable
        String nignxURI = ftpConfig.getNginxUri();
        String basePath = URICreator.pathToString(repository_path,"album",String.valueOf(albumId));
        String albumPath = URICreator.pathToString(basePath,"origin");
        File[] pictures = new File(albumPath).listFiles();
        if (pictures==null) {
            log.error("there are no files");
        }

        boolean isFirst = true;
        CameraModel createdCameraModel = null;
        AnalysisModel createdAnalysisModel = null;

        // createEntity
        // Camera , analysis , analysisCrack 분리시키기
        for (File picture : pictures) {
            if (isFirst) { // While 문으로 변경?
                Map<String, String> metadataMap = null;
                try {
                    metadataMap = extractMetadataFromImage(picture);
                } catch (ImageProcessingException | IOException e) {
                    log.error("could not extract metadata");
                    throw new RuntimeException(e);
                }

                CameraModel cameraModel = new CameraModel(metadataMap);
                createdCameraModel = cameraService.createEntity(cameraModel);
                AlbumModel albumModel = getEntityById(albumId);
                albumModel.setCameraId(createdCameraModel.getId());
                albumModel.setPhotoCount(pictures.length);
                updateEntity(albumId, albumModel);

                // if analysis exist -- 제거
                if (sourceInfo.isExistAnalysis()) {
                    AnalysisModel analysisModel = new AnalysisModel();
                    analysisModel.setAlbumId(albumId);
                    analysisModel.setName("crack");
                    analysisModel.setType(1);
                    analysisModel.setTypeName("Segmentation");
                    analysisModel.setStatus("Completed");
                    analysisModel.setCreatedDatetime(ZonedDateTime.now());
                    createdAnalysisModel = analysisService.createEntity(analysisModel);
                }
                isFirst = false;
            }
//            if (file.getName().equals("info.csv")) continue;

            AlbumPhotoModel albumPhotoModel = albumPhotoService.createEntity(new AlbumPhotoModel(albumId, createdCameraModel.getId(),
                    nignxURI + picture.getPath().substring(picture.getPath().indexOf("/album"))));

            // if analysis exist -- 제거
            if (sourceInfo.isExistAnalysis()) {
                AnalysisCrackModel analysisCrackModel = new AnalysisCrackModel();
                String jsonFilePath = nignxURI + picture.getPath().substring(picture.getPath().indexOf("/album"));
                jsonFilePath = jsonFilePath.replace("origin","analysis").replace(".JPG",".json");
                analysisCrackModel.setAlbumId(albumId);
                analysisCrackModel.setAnalysisId(createdAnalysisModel.getId());
                analysisCrackModel.setAnalysisType(1);
                analysisCrackModel.setCracksInfoPath(jsonFilePath);
                analysisCrackModel.setCreatedDatetime(ZonedDateTime.now());
                analysisCrackService.createEntity(analysisCrackModel);
            }

            albumPhotoPosService.createEntity(new AlbumPhotoPosModel(albumPhotoModel.getId(),
                    Paths.get(albumPhotoModel.getAlbumPhotoPath()).getFileName().toString(),
                    albumPhotoModel));
        }

        // resize pictures for thumbnail
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            boolean thumbnailResult = albumWorker.resizePictures(basePath,"thumbnails","300x200");
            if (thumbnailResult) {
                File[] thumbnailFiles = new File(basePath + "/thumbnails").listFiles();
                for (File file : thumbnailFiles) {
                    String thumbnailsPattern = file.getPath().substring(file.getPath().indexOf("/album")).replace("thumbnails", "origin");
                    AlbumPhotoModel resizedPhotoModel = albumPhotoService.getEntityByName(thumbnailsPattern);

                    String new_path = nignxURI + file.getPath().substring(file.getPath().indexOf("/album"));
                    resizedPhotoModel.setAlbumPhotoThumbnailsPath(new_path);
                    new_path = new_path.replace("thumbnails", "resized");
                    resizedPhotoModel.setAlbumPhotoResizedPath(new_path);
                    albumPhotoService.updateEntity(resizedPhotoModel.getId(), resizedPhotoModel);
                }
            } else {
                log.error("resize thumbnails error!");
            }
        });

        // resize pictures for analysis
        executorService.execute(() -> {
            boolean resizedResult = albumWorker.resizePictures(basePath,"resized","2100x1400");
            if (!resizedResult) {
                log.error("resize error!");
            }
        });
        executorService.shutdown(); // 스레드 풀 종료 시작

        return true;
    }

    public String exportAlbum(Integer albumId) {
        String basePath = URICreator.pathToString(repository_path, "album", String.valueOf(albumId));
        Path originPath = URICreator.pathTopath(basePath,"origin");
        Path analysisPath = URICreator.pathTopath(basePath,"analysis");

        albumResource.initialize(albumId);
        albumResource.includeImagesOn(originPath);

        if (Files.exists(analysisPath)) {
            albumResource.includeImagesOn(analysisPath);
        }

        String nignxURI = ftpConfig.getNginxUri();
        String newTargetPath = albumResource.getTargetPath().toString();
        newTargetPath = newTargetPath.substring(newTargetPath.indexOf("/album"));
        return URICreator.pathToString(nignxURI, newTargetPath);
    }
}