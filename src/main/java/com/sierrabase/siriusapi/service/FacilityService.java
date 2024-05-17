package com.sierrabase.siriusapi.service;

import com.sierrabase.siriusapi.entity.FacilityEntity;
import com.sierrabase.siriusapi.model.FacilityModel;
import com.sierrabase.siriusapi.repository.FacilityEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FacilityService {
    @Autowired
    private FacilityEntityRepository facilityEntityRepository;

    public ArrayList<FacilityModel> getAllEntities()  {
        List<FacilityEntity> entities = facilityEntityRepository.findAll();
        if(entities.size() <= 0)
            return null;

        ArrayList<FacilityModel> modelList = new ArrayList<FacilityModel>();
        for (FacilityEntity entity : entities) {
            modelList.add(new FacilityModel(entity));
        }

        return modelList;
    }

    public FacilityModel getEntityById(Integer id) {
        Optional<FacilityEntity> entity = facilityEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        FacilityModel model = new FacilityModel(entity.get());

        return model;
    }


    public FacilityModel createEntity(FacilityModel facilityModel) {
        FacilityEntity entity = new FacilityEntity(facilityModel);
        // Set properties from facilityModel to entity

        entity = facilityEntityRepository.save(entity);
        return new FacilityModel(entity);
    }

    public FacilityModel updateEntity(Integer id, FacilityModel facilityModel) {
        Optional<FacilityEntity> optionalEntity = facilityEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            FacilityEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = facilityEntityRepository.save(new FacilityEntity(entity.getFacility_id(), facilityModel));
            return new FacilityModel(entity);
        } else {
            log.error("Facility not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<FacilityEntity> optionalEntity = facilityEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            facilityEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("Facility not found with id: {}", id);
            return false;
        }
    }
}
