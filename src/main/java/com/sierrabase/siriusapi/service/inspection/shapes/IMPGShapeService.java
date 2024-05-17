package com.sierrabase.siriusapi.service.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IMPGShapeService {
    @Autowired
    private IMPGShapeEntityRepository impgShapeEntityRepository;

    public ArrayList<IMPGShapeModel> getAllEntitiesByImpId(Integer impId) {
        List<IMPGShapeEntity> entities = impgShapeEntityRepository.findAllByImpId(impId);

        if(entities.size() <= 0)
            return null;

        ArrayList<IMPGShapeModel> modelList = new ArrayList<IMPGShapeModel>();
        for (IMPGShapeEntity entity : entities) {
            modelList.add(new IMPGShapeModel(entity));
        }

        return modelList;
    }

    public IMPGShapeModel getEntityById(Integer id) {
        Optional<IMPGShapeEntity> entity = impgShapeEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        IMPGShapeModel model = new IMPGShapeModel(entity.get());

        return model;
    }

    public IMPGShapeModel createEntity(IMPGShapeModel impgShapeModel) {
        IMPGShapeEntity entity = new IMPGShapeEntity(impgShapeModel);
        // Set properties from impModel to entity

        entity = impgShapeEntityRepository.save(entity);
        return new IMPGShapeModel(entity);
    }


    public IMPGShapeModel updateEntity(Integer id, IMPGShapeModel impgShapeModel) {
        Optional<IMPGShapeEntity> optionalEntity = impgShapeEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            IMPGShapeEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = impgShapeEntityRepository.save(new IMPGShapeEntity(entity.getImpg_shape_id(), impgShapeModel));
            return new IMPGShapeModel(entity);
        } else {
            log.error("IMPGS not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<IMPGShapeEntity> optionalEntity = impgShapeEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            impgShapeEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("IMPGS not found with id: {}", id);
            return false;
        }
    }
}
