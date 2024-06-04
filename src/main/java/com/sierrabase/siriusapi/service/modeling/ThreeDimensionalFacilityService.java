package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalFacilityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ThreeDimensionalFacilityService {
    @Autowired
    private ThreeDimensionalFacilityRepository threeDimensionalFacilityRepository;
    @Autowired
    private ThreeDimensionalFacilityWorker threeDimensionalFacilityWorker;
    @Autowired
    private FtpConfig ftpConfig;
    @Value("${path.repository.base}")
    private String repository_path;

    public ArrayList<ThreeDimensionalFacilityModel> getAllEntities(Integer facilityId)  {

        List<ThreeDimensionalFacilityEntity> entities = threeDimensionalFacilityRepository.findAllByFacilityId(facilityId);
        if(entities.size() <= 0)
            return null;

        ArrayList<ThreeDimensionalFacilityModel> modelList = new ArrayList<ThreeDimensionalFacilityModel>();
        for (ThreeDimensionalFacilityEntity entity : entities) {
            modelList.add(new ThreeDimensionalFacilityModel(entity));
        }

        return modelList;
    }

    public ThreeDimensionalFacilityModel getEntityById(Integer id) {
        Optional<ThreeDimensionalFacilityEntity> entity = threeDimensionalFacilityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        ThreeDimensionalFacilityModel model = new ThreeDimensionalFacilityModel(entity.get());

        return model;
    }


    public ThreeDimensionalFacilityModel createEntity(ThreeDimensionalFacilityModel model) {
        ThreeDimensionalFacilityEntity entity = new ThreeDimensionalFacilityEntity(model);
        // Set properties from facilityModel to entity

        entity = threeDimensionalFacilityRepository.save(entity);
        return new ThreeDimensionalFacilityModel(entity);
    }

    public ThreeDimensionalFacilityModel updateEntity(Integer id, ThreeDimensionalFacilityModel model) {
        Optional<ThreeDimensionalFacilityEntity> optionalEntity = threeDimensionalFacilityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ThreeDimensionalFacilityEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = threeDimensionalFacilityRepository.save(new ThreeDimensionalFacilityEntity(entity.getThree_dimensional_facility_id(), model));
            return new ThreeDimensionalFacilityModel(entity);
        } else {
            log.error("3D Model not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<ThreeDimensionalFacilityEntity> optionalEntity = threeDimensionalFacilityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            threeDimensionalFacilityRepository.deleteById(id);
            return true;
        } else {
            log.error("3D Model not found with id: {}", id);
            return false;
        }
    }

    public boolean importModel(Integer id, SourceInfoModel model) {
        boolean importResult = threeDimensionalFacilityWorker.importModel(id, model.getUrl());
        if (!importResult) {
            log.error("Import model Error");
        }
        return true;
    }

    public boolean createGLTF(ThreeDimensionalFacilityModel model) {
        String basePath = URICreator.pathToString(repository_path,"model", String.valueOf(model.getId()));
        boolean createResult = threeDimensionalFacilityWorker.createGLTF(basePath);
        if (!createResult) {
            log.error("Can not create GLTF File");
        }
        log.info("create gltf success");
        return true;
    }

    public String getElevationURL(Integer id) {
        String targetPath = URICreator.pathToString(repository_path,"model",String.valueOf(id),"elevation",id+".zip");
        Path path = Paths.get(targetPath);

        if (Files.exists(path)) {
            targetPath = URICreator.pathToString(ftpConfig.getNginxUri(),targetPath.substring(targetPath.indexOf("/model")));
            return targetPath;
        } else {
            return null;
        }
    }
}
