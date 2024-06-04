package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityRoiEntity;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityRoiModel;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalFacilityInfoRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ThreeDimensionalFacilityRoiService {
    @Autowired
    private ThreeDimensionalFacilityInfoRepository threeDimensionalFacilityInfoRepository;

    public ArrayList<ThreeDimensionalFacilityRoiModel> getAllEntities(Integer modelId)  {
        List<ThreeDimensionalFacilityRoiEntity> entities = threeDimensionalFacilityInfoRepository.findAllByThreeDimensionalFacilityId(modelId);
        if(entities.size() <= 0)
            return null;

        ArrayList<ThreeDimensionalFacilityRoiModel> modelList = new ArrayList<ThreeDimensionalFacilityRoiModel>();
        for (ThreeDimensionalFacilityRoiEntity entity : entities) {
            modelList.add(new ThreeDimensionalFacilityRoiModel(entity));
        }

        return modelList;
    }

    public ThreeDimensionalFacilityRoiModel getEntityById(Integer id) {
        Optional<ThreeDimensionalFacilityRoiEntity> entity = threeDimensionalFacilityInfoRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        ThreeDimensionalFacilityRoiModel model = new ThreeDimensionalFacilityRoiModel(entity.get());

        return model;
    }


    public ThreeDimensionalFacilityRoiModel createEntity(ThreeDimensionalFacilityRoiModel model) {
        ThreeDimensionalFacilityRoiEntity entity = new ThreeDimensionalFacilityRoiEntity(model);
        // Set properties from facilityModel to entity

        entity = threeDimensionalFacilityInfoRepository.save(entity);
        return new ThreeDimensionalFacilityRoiModel(entity);
    }

    public ThreeDimensionalFacilityRoiModel updateEntity(Integer id, ThreeDimensionalFacilityRoiModel model) {
        Optional<ThreeDimensionalFacilityRoiEntity> optionalEntity = threeDimensionalFacilityInfoRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ThreeDimensionalFacilityRoiEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = threeDimensionalFacilityInfoRepository.save(new ThreeDimensionalFacilityRoiEntity(entity.getThree_dimensional_facility_id(), model));
            return new ThreeDimensionalFacilityRoiModel(entity);
        } else {
            log.error("3D Model Info not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<ThreeDimensionalFacilityRoiEntity> optionalEntity = threeDimensionalFacilityInfoRepository.findById(id);
        if (optionalEntity.isPresent()) {
            threeDimensionalFacilityInfoRepository.deleteById(id);
            return true;
        } else {
            log.error("3D Model Info not found with id: {}", id);
            return false;
        }
    }
}
