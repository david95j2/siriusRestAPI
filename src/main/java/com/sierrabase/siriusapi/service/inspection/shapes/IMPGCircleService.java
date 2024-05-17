package com.sierrabase.siriusapi.service.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCirclePropertyEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCommonPropertyEntity;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGCircleModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeCirclePropertyEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeCommonPropertyEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IMPGCircleService {
    @Autowired
    private IMPGShapeEntityRepository impgShapeEntityRepository;
    @Autowired
    private IMPGShapeCommonPropertyEntityRepository impgShapeCommonPropertyEntityRepository;
    @Autowired
    private IMPGShapeCirclePropertyEntityRepository impgShapeCirclePropertyEntityRepository;

    public IMPGCircleModel getEntityById(Integer sId) {
        Optional<IMPGShapeEntity> shapeEntity = impgShapeEntityRepository.findById(sId);
        if(!shapeEntity.isPresent())
            return null;

        IMPGShapeModel shapeModel = new IMPGShapeModel(shapeEntity.get());
        Optional<IMPGShapeCommonPropertyEntity> shapeCommonPropertyEntity = impgShapeCommonPropertyEntityRepository.findIMPGShapeCommonPropertyEntityByImpgShapeId(shapeModel.getId());
        Optional<IMPGShapeCirclePropertyEntity> circlePropertyEntity = impgShapeCirclePropertyEntityRepository.findIMPGShapeCirclePropertyEntityByImpgShapeId(shapeModel.getId());
        if(!shapeCommonPropertyEntity.isPresent() || !circlePropertyEntity.isPresent())
            return null;

        IMPGShapeCommonPropertyModel shapeCommonPropertyModel = new IMPGShapeCommonPropertyModel(shapeCommonPropertyEntity.get());
        IMPGCircleModel model = new IMPGCircleModel(shapeModel, shapeCommonPropertyModel, circlePropertyEntity.get());


        return model;
    }

    public IMPGCircleModel createEntity(IMPGCircleModel impgCircleModel) {

        // Create Circle Property
        IMPGShapeCirclePropertyEntity entity = new IMPGShapeCirclePropertyEntity(impgCircleModel);
        entity = impgShapeCirclePropertyEntityRepository.save(entity);

        return new IMPGCircleModel(impgCircleModel.getShape(), impgCircleModel.getCommonProperty(), entity);
    }

    public IMPGCircleModel updateEntity(Integer id, IMPGCircleModel impgCircleModel) {
        Optional<IMPGShapeCirclePropertyEntity> optionalEntity = impgShapeCirclePropertyEntityRepository.findIMPGShapeCirclePropertyEntityByImpgShapeId(id);
        if(optionalEntity.isPresent()) {
            IMPGShapeCirclePropertyEntity entity = optionalEntity.get();

            entity = impgShapeCirclePropertyEntityRepository.save(new IMPGShapeCirclePropertyEntity(entity.getImpg_shape_cirlce_property_id(), impgCircleModel));
            log.info("test : " + entity);
            return new IMPGCircleModel(impgCircleModel.getShape(), impgCircleModel.getCommonProperty(), entity);
        } else {
            log.error("Circle not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {

        Optional<IMPGShapeCirclePropertyEntity> optionalEntity = impgShapeCirclePropertyEntityRepository.findIMPGShapeCirclePropertyEntityByImpgShapeId(id);
        if (optionalEntity.isPresent()) {

            impgShapeCirclePropertyEntityRepository.deleteById(optionalEntity.get().getImpg_shape_cirlce_property_id());
            return true;
        } else {
            log.error("Circle not found with id: {}", id);
            return false;
        }
    }
}
