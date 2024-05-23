package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityInfoEntity;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityInfoModel;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalFacilityInfoRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ThreeDimensionalFacilityInfoService {
    @Autowired
    private ThreeDimensionalFacilityInfoRepository threeDimensionalFacilityInfoRepository;

    public ArrayList<ThreeDimensionalFacilityInfoModel> getAllEntities(Integer modelId)  {
        List<ThreeDimensionalFacilityInfoEntity> entities = threeDimensionalFacilityInfoRepository.findAllByThreeDimensionalFacilityId(modelId);
        if(entities.size() <= 0)
            return null;

        ArrayList<ThreeDimensionalFacilityInfoModel> modelList = new ArrayList<ThreeDimensionalFacilityInfoModel>();
        for (ThreeDimensionalFacilityInfoEntity entity : entities) {
            modelList.add(new ThreeDimensionalFacilityInfoModel(entity));
        }

        return modelList;
    }

    public ThreeDimensionalFacilityInfoModel getEntityById(Integer id) {
        Optional<ThreeDimensionalFacilityInfoEntity> entity = threeDimensionalFacilityInfoRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        ThreeDimensionalFacilityInfoModel model = new ThreeDimensionalFacilityInfoModel(entity.get());

        return model;
    }


    public ThreeDimensionalFacilityInfoModel createEntity(ThreeDimensionalFacilityInfoModel model) {
        ThreeDimensionalFacilityInfoEntity entity = new ThreeDimensionalFacilityInfoEntity(model);
        // Set properties from facilityModel to entity

        entity = threeDimensionalFacilityInfoRepository.save(entity);
        return new ThreeDimensionalFacilityInfoModel(entity);
    }

    public ThreeDimensionalFacilityInfoModel updateEntity(Integer id, ThreeDimensionalFacilityInfoModel model) {
        Optional<ThreeDimensionalFacilityInfoEntity> optionalEntity = threeDimensionalFacilityInfoRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ThreeDimensionalFacilityInfoEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = threeDimensionalFacilityInfoRepository.save(new ThreeDimensionalFacilityInfoEntity(entity.getThree_dimensional_facility_id(), model));
            return new ThreeDimensionalFacilityInfoModel(entity);
        } else {
            log.error("3D Model Info not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<ThreeDimensionalFacilityInfoEntity> optionalEntity = threeDimensionalFacilityInfoRepository.findById(id);
        if (optionalEntity.isPresent()) {
            threeDimensionalFacilityInfoRepository.deleteById(id);
            return true;
        } else {
            log.error("3D Model Info not found with id: {}", id);
            return false;
        }
    }
}
