package com.sierrabase.siriusapi.service.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCommonPropertyEntity;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeUndersidePropertyEntity;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGUndersideModel;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeCommonPropertyEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeUndersidePropertyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IMPGUndersideService {
    @Autowired
    private IMPGShapeEntityRepository impgShapeEntityRepository;
    @Autowired
    private IMPGShapeCommonPropertyEntityRepository impgShapeCommonPropertyEntityRepository;
    @Autowired
    private IMPGShapeUndersidePropertyEntityRepository impgShapeUndersidePropertyEntityRepository;

    public IMPGUndersideModel getEntityById(Integer sId) {
        Optional<IMPGShapeEntity> shapeEntity = impgShapeEntityRepository.findById(sId);
        if(!shapeEntity.isPresent())
            return null;
        IMPGShapeModel shapeModel = new IMPGShapeModel(shapeEntity.get());
        Optional<IMPGShapeCommonPropertyEntity> shapeCommonPropertyEntity = impgShapeCommonPropertyEntityRepository.findIMPGShapeCommonPropertyEntityByImpgShapeId(shapeModel.getId());
        Optional<IMPGShapeUndersidePropertyEntity> undersidePropertyEntity = impgShapeUndersidePropertyEntityRepository.findIMPGShapeUndersidePropertyEntityByImpgShapeId(shapeModel.getId());
        if(!shapeCommonPropertyEntity.isPresent() || !undersidePropertyEntity.isPresent())
            return null;
        IMPGShapeCommonPropertyModel shapeCommonPropertyModel = new IMPGShapeCommonPropertyModel(shapeCommonPropertyEntity.get());
        IMPGUndersideModel model = new IMPGUndersideModel(shapeModel, shapeCommonPropertyModel, undersidePropertyEntity.get());
        return model;
    }

    public IMPGUndersideModel createEntity(IMPGUndersideModel impgUndersideModel) {
        // Create Rectangle Property
        IMPGShapeUndersidePropertyEntity entity = new IMPGShapeUndersidePropertyEntity(impgUndersideModel);
        entity = impgShapeUndersidePropertyEntityRepository.save(entity);

        return new IMPGUndersideModel(impgUndersideModel.getShape(), impgUndersideModel.getCommonProperty(), entity);
    }

    public IMPGUndersideModel updateEntity(Integer id, IMPGUndersideModel impgUndersideModel) {
        Optional<IMPGShapeUndersidePropertyEntity> optionalEntity = impgShapeUndersidePropertyEntityRepository.findIMPGShapeUndersidePropertyEntityByImpgShapeId(id);
        if(optionalEntity.isPresent()) {
            IMPGShapeUndersidePropertyEntity entity = optionalEntity.get();

            entity = impgShapeUndersidePropertyEntityRepository.save(new IMPGShapeUndersidePropertyEntity(entity.getImpg_shape_underside_property_id(), impgUndersideModel));
            return new IMPGUndersideModel(impgUndersideModel.getShape(), impgUndersideModel.getCommonProperty(), entity);
        } else {
            log.error("Underside not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {

        Optional<IMPGShapeUndersidePropertyEntity> optionalEntity = impgShapeUndersidePropertyEntityRepository.findIMPGShapeUndersidePropertyEntityByImpgShapeId(id);
        if (optionalEntity.isPresent()) {
            impgShapeUndersidePropertyEntityRepository.deleteById(optionalEntity.get().getImpg_shape_underside_property_id());
            return true;
        } else {
            log.error("Underside not found with id: {}", id);
            return false;
        }
    }
}
