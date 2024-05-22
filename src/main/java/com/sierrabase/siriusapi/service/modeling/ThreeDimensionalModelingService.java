package com.sierrabase.siriusapi.service.modeling;



import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelingEntity;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModelingModel;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalFacilityRepository;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalModelingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ThreeDimensionalModelingService {
    @Autowired
    private ThreeDimensionalModelingRepository threeDimensionalModelingRepository;
    @Autowired
    private ThreeDimensionalFacilityRepository threeDimensionalFacilityRepository;

    public ArrayList<ThreeDimensionalModelingModel> getAllEntities(Integer id)  {
        List<ThreeDimensionalModelingEntity> entities = threeDimensionalModelingRepository.findAllByAlbumId(id);
        if(entities.size() <= 0)
            return null;

        ArrayList<ThreeDimensionalModelingModel> modelList = new ArrayList<ThreeDimensionalModelingModel>();
        for (ThreeDimensionalModelingEntity entity : entities) {
            modelList.add(new ThreeDimensionalModelingModel(entity));
        }

        return modelList;
    }

    public ThreeDimensionalModelingModel getEntityById(Integer id) {
        Optional<ThreeDimensionalModelingEntity> entity = threeDimensionalModelingRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        ThreeDimensionalModelingModel model = new ThreeDimensionalModelingModel(entity.get());

        return model;
    }


    public ThreeDimensionalModelingModel createEntity(ThreeDimensionalModelingModel model) {
        ThreeDimensionalModelingEntity entity = new ThreeDimensionalModelingEntity(model);
        // Set properties from facilityModel to entity

        entity = threeDimensionalModelingRepository.save(entity);
        return new ThreeDimensionalModelingModel(entity);
    }

    public ThreeDimensionalModelingModel updateEntity(Integer id, ThreeDimensionalModelingModel model) {
        Optional<ThreeDimensionalModelingEntity> optionalEntity = threeDimensionalModelingRepository.findById(id);
        if (optionalEntity.isPresent()) {
            ThreeDimensionalModelingEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = threeDimensionalModelingRepository.save(new ThreeDimensionalModelingEntity(entity.getThree_dimensional_modeling_id(), model));
            return new ThreeDimensionalModelingModel(entity);
        } else {
            log.error("3D Modeling not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<ThreeDimensionalModelingEntity> optionalEntity = threeDimensionalModelingRepository.findById(id);
        if (optionalEntity.isPresent()) {
            threeDimensionalModelingRepository.deleteById(id);
            return true;
        } else {
            log.error("3D Modeling not found with id: {}", id);
            return false;
        }
    }
}
