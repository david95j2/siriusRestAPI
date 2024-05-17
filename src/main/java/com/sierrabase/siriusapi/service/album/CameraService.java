package com.sierrabase.siriusapi.service.album;

import com.sierrabase.siriusapi.entity.album.CameraEntity;
import com.sierrabase.siriusapi.model.album.CameraModel;

import com.sierrabase.siriusapi.repository.album.CameraEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CameraService {
    @Autowired
    private CameraEntityRepository cameraEntityRepository;

    public ArrayList<CameraModel> getAllEntities() {
        List<CameraEntity> entities = cameraEntityRepository.findAll();

        if(entities.size() <= 0)
            return null;

        ArrayList<CameraModel> modelList = new ArrayList<CameraModel>();
        for (CameraEntity entity : entities) {
            modelList.add(new CameraModel(entity));
        }

        return modelList;
    }

    public CameraModel getEntityById(Integer id) {
        Optional<CameraEntity> entity = cameraEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        CameraModel model = new CameraModel(entity.get());

        return model;
    }

    public CameraModel createEntity(CameraModel cameraModel) {
        CameraEntity entity = new CameraEntity(cameraModel);
        // Set properties from cameraModel to entity

        entity = cameraEntityRepository.save(entity);
        return new CameraModel(entity);
    }

    public CameraModel updateEntity(Integer id, CameraModel cameraModel) {
        Optional<CameraEntity> optionalEntity = cameraEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            CameraEntity entity = optionalEntity.get();
            // Update properties from cameraModel to entity

            entity = cameraEntityRepository.save(new CameraEntity(entity.getCamera_id(), cameraModel));
            return new CameraModel(entity);
        } else {
            log.error("Camera not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<CameraEntity> optionalEntity = cameraEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            cameraEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("Camera not found with id: {}", id);
            return false;
        }
    }
}