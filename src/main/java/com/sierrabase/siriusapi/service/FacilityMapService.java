package com.sierrabase.siriusapi.service;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.entity.FacilityMapEntity;
import com.sierrabase.siriusapi.model.FacilityMapModel;
import com.sierrabase.siriusapi.repository.FacilityMapEntityRepository;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FacilityMapService {
    @Autowired
    private FacilityMapEntityRepository facilityMapEntityRepository;
    @Autowired
    private FacilityWorker facilityWorker;


    public ArrayList<FacilityMapModel> getEntitiesByFacilityId(Integer facilityId) {
        List<FacilityMapEntity> entities = facilityMapEntityRepository.findAllByFacilityId(facilityId);

        if (entities.size() <= 0)
            return null;

        ArrayList<FacilityMapModel> modelList = new ArrayList<FacilityMapModel>();
        for (FacilityMapEntity entity : entities) {
            modelList.add(new FacilityMapModel(entity));
        }

        return modelList;
    }

    public FacilityMapModel getEntityById(Integer id) {
        Optional<FacilityMapEntity> entity = facilityMapEntityRepository.findById(id);

        log.debug("entity", entity);

        if (!entity.isPresent())
            return null;

        FacilityMapModel model = new FacilityMapModel(entity.get());

        return model;
    }

    public FacilityMapModel createEntity(FacilityMapModel facilityMapModel) {
        FacilityMapEntity entity = new FacilityMapEntity(facilityMapModel);
        // Set properties from facilityMapModel to entity

        entity = facilityMapEntityRepository.save(entity);
        return new FacilityMapModel(entity);
    }

    public FacilityMapModel updateEntity(Integer id, FacilityMapModel facilityMapModel) {
        Optional<FacilityMapEntity> optionalEntity = facilityMapEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            FacilityMapEntity entity = optionalEntity.get();
            // Update properties from facilityMapModel to entity

            entity = facilityMapEntityRepository.save(new FacilityMapEntity(entity.getFacility_map_id(), facilityMapModel));
            return new FacilityMapModel(entity);
        } else {
            log.error("FacilityMap not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<FacilityMapEntity> optionalEntity = facilityMapEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            facilityMapEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("FacilityMap not found with id: {}", id);
            return false;
        }
    }

    //    public Boolean uploadFile(MultipartFile file, String directoryPath) {
//
//        try {
//            File directory = new File(directoryPath);
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            Path tempDir = Files.createTempDirectory("resized-images");
//            File processedImage = new File(tempDir.toFile(), file.getOriginalFilename().replaceAll(" ", ""));
//            BufferedImage inputImage = ImageIO.read(file);
//            BufferedImage outputImage = new BufferedImage(300, 200, inputImage.getType());
//            ImageIO.write(outputImage, "JPEG", processedImage);
//
////            Path path = Paths.get(directoryPath, file.getOriginalFilename().replaceAll(" ",""));
////            Files.write(path, file.getBytes());
//
//            return true;
//
//        } catch (IOException e) {
//            log.error("File Upload failed : ", e);
//            return false;
//        }
//    }
    public Boolean uploadFile(MultipartFile file, Integer facilityId) {
        boolean uploadResult = facilityWorker.uploadFile(file, facilityId);
        if (!uploadResult) {
            log.error("can not upload facility thumbnails");
            return false;
        }
        return true;
    }


}
