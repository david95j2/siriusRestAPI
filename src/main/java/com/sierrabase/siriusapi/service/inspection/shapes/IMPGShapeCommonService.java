package com.sierrabase.siriusapi.service.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCommonPropertyEntity;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeCommonPropertyEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IMPGShapeCommonService {
    @Autowired
    private IMPGShapeCommonPropertyEntityRepository impgShapeCommonPropertyEntityRepository;


    public IMPGShapeCommonPropertyModel getEntityById(Integer id) {
        Optional<IMPGShapeCommonPropertyEntity> entity = impgShapeCommonPropertyEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        IMPGShapeCommonPropertyModel model = new IMPGShapeCommonPropertyModel(entity.get());

        return model;
    }

    public IMPGShapeCommonPropertyModel createEntity(IMPGShapeModel shapeModel, IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel) {
        IMPGShapeCommonPropertyEntity entity = new IMPGShapeCommonPropertyEntity(impgShapeCommonPropertyModel,shapeModel);
        // Set properties from impModel to entity

        entity = impgShapeCommonPropertyEntityRepository.save(entity);
        return new IMPGShapeCommonPropertyModel(entity);
    }

    public IMPGShapeCommonPropertyModel updateEntity(IMPGShapeModel impgShapeModel, IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel) {
        Optional<IMPGShapeCommonPropertyEntity> optionalEntity = impgShapeCommonPropertyEntityRepository.findIMPGShapeCommonPropertyEntityByImpgShapeId(impgShapeModel.getId());
        if(optionalEntity.isPresent()) {
            IMPGShapeCommonPropertyEntity entity = optionalEntity.get();
            entity = impgShapeCommonPropertyEntityRepository.save(new IMPGShapeCommonPropertyEntity(entity.getImpg_shape_common_property_id(), impgShapeCommonPropertyModel,impgShapeModel));
            return new IMPGShapeCommonPropertyModel(entity);
        } else {
            log.error("IMPGSC not found with id: {}", impgShapeModel.getImpgId());
            return null;
        }

    }

    public boolean deleteEntity(Integer id) {
        Optional<IMPGShapeCommonPropertyEntity> optionalEntity = impgShapeCommonPropertyEntityRepository.findIMPGShapeCommonPropertyEntityByImpgShapeId(id);
        if (optionalEntity.isPresent()) {
            impgShapeCommonPropertyEntityRepository.deleteById(optionalEntity.get().getImpg_shape_common_property_id());
            return true;
        } else {
            log.error("IMPGSC not found with id: {}", id);
            return false;
        }
    }


//    public IMPGShapeModel updateEntity(Integer id, IMPGShapeModel impgShapeModel) {
//        Optional<IMPGShapeEntity> optionalEntity = impgShapeEntityRepository.findById(id);
//        if (optionalEntity.isPresent()) {
//            IMPGShapeEntity entity = optionalEntity.get();
//            // Update properties from facilityModel to entity
//
//            entity = impgShapeEntityRepository.save(new IMPGShapeEntity(entity.getImpg_shape_id(), impgShapeModel));
//            return new IMPGShapeModel(entity);
//        } else {
//            log.error("IMPS not found with id: {}", id);
//            return null;
//        }
//    }
//
//    public boolean deleteEntity(Integer id) {
//        Optional<IMPGShapeEntity> optionalEntity = impgShapeEntityRepository.findById(id);
//        if (optionalEntity.isPresent()) {
//            impgShapeEntityRepository.deleteById(id);
//            return true;
//        } else {
//            log.error("IMPS not found with id: {}", id);
//            return false;
//        }
//    }
}
