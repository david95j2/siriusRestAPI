package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityInfoEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelEntity;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityInfoModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModel;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalFacilityInfoRepository;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalModelRepository;
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
public class ThreeDimensionalModelService {
    @Autowired
    private ThreeDimensionalModelRepository threeDimensionalModelRepository;
    @Autowired
    private FtpConfig ftpConfig;

    @Value("${path.repository.base}")
    private String repository_path;

    public ArrayList<ThreeDimensionalModel> getAllEntities(Integer facilityId)  {
        List<ThreeDimensionalModelEntity> entities = threeDimensionalModelRepository.findAllByFacilityId(facilityId);
        if(entities.size() <= 0)
            return null;

        ArrayList<ThreeDimensionalModel> modelList = new ArrayList<ThreeDimensionalModel>();
        for (ThreeDimensionalModelEntity entity : entities) {
            modelList.add(new ThreeDimensionalModel(entity));
        }

        return modelList;
    }

    public ThreeDimensionalModel getEntityById(Integer id) {
        Optional<ThreeDimensionalModelEntity> entity = threeDimensionalModelRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        ThreeDimensionalModel model = new ThreeDimensionalModel(entity.get());

        return model;
    }


    public ThreeDimensionalModel createEntity(ThreeDimensionalModel model) {
        ThreeDimensionalModelEntity entity = new ThreeDimensionalModelEntity(model);
        // Set properties from facilityModel to entity

        entity = threeDimensionalModelRepository.save(entity);
        return new ThreeDimensionalModel(entity);
    }

    public ThreeDimensionalModel updateEntity(Integer id, ThreeDimensionalModel model) {
        Optional<ThreeDimensionalModelEntity> optionalEntity = threeDimensionalModelRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ThreeDimensionalModelEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = threeDimensionalModelRepository.save(new ThreeDimensionalModelEntity(entity.getThree_dimensional_model_id(), model));
            return new ThreeDimensionalModel(entity);
        } else {
            log.error("3D Model for CAD not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<ThreeDimensionalModelEntity> optionalEntity = threeDimensionalModelRepository.findById(id);
        if (optionalEntity.isPresent()) {
            threeDimensionalModelRepository.deleteById(id);
            return true;
        } else {
            log.error("3D Model for CAD Info not found with id: {}", id);
            return false;
        }
    }

    public String getElevationURL(Integer model_id) {
        String targetPath = URICreator.pathToString(repository_path,"model",String.valueOf(model_id),"elevation",model_id+".zip");
        Path path = Paths.get(targetPath);

        if (Files.exists(path)) {
            targetPath = URICreator.pathToString(ftpConfig.getNginxUri(),targetPath.substring(targetPath.indexOf("/model")));
            return targetPath;
        } else {
            return null;
        }
    }
}
