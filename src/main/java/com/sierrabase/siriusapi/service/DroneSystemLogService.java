package com.sierrabase.siriusapi.service;

import com.sierrabase.siriusapi.entity.DroneSystemLogEntity;
import com.sierrabase.siriusapi.entity.LoginAttemptsEntity;
import com.sierrabase.siriusapi.model.DroneSystemLogModel;
import com.sierrabase.siriusapi.model.LoginAttemptModel;
import com.sierrabase.siriusapi.repository.DroneSystemLogEntityRepository;
import com.sierrabase.siriusapi.repository.LoginAttemptEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DroneSystemLogService {
    @Autowired
    private DroneSystemLogEntityRepository droneSystemLogEntityRepository;

    public ArrayList<DroneSystemLogModel> getAllEntities() {
        List<DroneSystemLogEntity> entities = droneSystemLogEntityRepository.findAll();

        if (entities.size() <= 0)
            return null;

        ArrayList<DroneSystemLogModel> modelList = new ArrayList<DroneSystemLogModel>();
        for (DroneSystemLogEntity entity : entities) {
            modelList.add(new DroneSystemLogModel(entity));
        }

        return modelList;
    }

    public DroneSystemLogModel getEntityById(Integer id) {
        Optional<DroneSystemLogEntity> entity = droneSystemLogEntityRepository.findById(id);

        if (!entity.isPresent())
            return null;

        DroneSystemLogModel model = new DroneSystemLogModel(entity.get());

        return model;
    }

    public DroneSystemLogModel createEntity(DroneSystemLogModel model) {
        DroneSystemLogEntity entity = new DroneSystemLogEntity(model);
        // Set properties from facilityMapModel to entity

        entity = droneSystemLogEntityRepository.save(entity);
        return new DroneSystemLogModel(entity);
    }

    public DroneSystemLogModel updateEntity(Integer id, DroneSystemLogModel model) {
        Optional<DroneSystemLogEntity> optionalEntity = droneSystemLogEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            DroneSystemLogEntity entity = optionalEntity.get();
            // Update properties from facilityMapModel to entity

            entity = droneSystemLogEntityRepository.save(new DroneSystemLogEntity(entity.getDroneSystemLogId(), model));
            return new DroneSystemLogModel(entity);
        } else {
            log.error("LoginAttempts not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<DroneSystemLogEntity> optionalEntity = droneSystemLogEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            droneSystemLogEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("LoginAttempts not found with id: {}", id);
            return false;
        }
    }
}
