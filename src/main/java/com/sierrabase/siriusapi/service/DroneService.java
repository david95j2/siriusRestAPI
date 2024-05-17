package com.sierrabase.siriusapi.service;

import com.sierrabase.siriusapi.entity.DroneTypeEntity;
import com.sierrabase.siriusapi.entity.FacilityEntity;
import com.sierrabase.siriusapi.model.DroneTypeModel;
import com.sierrabase.siriusapi.model.FacilityModel;
import com.sierrabase.siriusapi.repository.DroneTypeEntityRepository;
import com.sierrabase.siriusapi.repository.FacilityEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DroneService {
    @Autowired
    private DroneTypeEntityRepository droneTypeEntityRepository;

    public ArrayList<DroneTypeModel> getAllEntities() {
        // DB상에 존재하는 모든 드론 종류 목록 정보를 가져와서 반환할 것
        List<DroneTypeEntity> entities = droneTypeEntityRepository.findAll();
        if (entities.size() <= 0)
            return null;

        ArrayList<DroneTypeModel> modelList = new ArrayList<DroneTypeModel>();
        for (DroneTypeEntity entity : entities) {
            modelList.add(new DroneTypeModel(entity));
        }

        return modelList;
    }

    public DroneTypeModel getEntityById(Integer id) {
        Optional<DroneTypeEntity> entity = droneTypeEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        DroneTypeModel model = new DroneTypeModel(entity.get());

        return model;
    }

    public DroneTypeModel createEntity(DroneTypeModel droneTypeModel) {
        DroneTypeEntity entity = new DroneTypeEntity(droneTypeModel);
        // Set properties from facilityModel to entity

        entity = droneTypeEntityRepository.save(entity);
        return new DroneTypeModel(entity);
    }

    public DroneTypeModel updateEntity(Integer id, DroneTypeModel droneTypeModel) {
        Optional<DroneTypeEntity> optionalEntity = droneTypeEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            DroneTypeEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = droneTypeEntityRepository.save(new DroneTypeEntity(entity.getDrone_type_id(), droneTypeModel));
            return new DroneTypeModel(entity);
        } else {
            log.error("DroneType not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<DroneTypeEntity> optionalEntity = droneTypeEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            droneTypeEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("DroneType not found with id: {}", id);
            return false;
        }
    }
}
