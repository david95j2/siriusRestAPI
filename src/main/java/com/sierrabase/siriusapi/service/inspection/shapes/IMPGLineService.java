package com.sierrabase.siriusapi.service.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCommonPropertyEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeLinePropertyEntity;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGLineModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeCommonPropertyEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeLinePropertyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IMPGLineService {
    @Autowired
    private IMPGShapeEntityRepository impgShapeEntityRepository;
    @Autowired
    private IMPGShapeCommonPropertyEntityRepository impgShapeCommonPropertyEntityRepository;
    @Autowired
    private IMPGShapeLinePropertyEntityRepository impgShapeLinePropertyEntityRepository;

    public IMPGLineModel getEntityById(Integer sId) {
        Optional<IMPGShapeEntity> shapeEntity = impgShapeEntityRepository.findById(sId);
        if(!shapeEntity.isPresent())
            return null;

        IMPGShapeModel shapeModel = new IMPGShapeModel(shapeEntity.get());
        Optional<IMPGShapeCommonPropertyEntity> shapeCommonPropertyEntity = impgShapeCommonPropertyEntityRepository.findIMPGShapeCommonPropertyEntityByImpgShapeId(shapeModel.getId());
        Optional<IMPGShapeLinePropertyEntity> linePropertyEntity = impgShapeLinePropertyEntityRepository.findIMPGShapeLinePropertyEntityByImpgShapeId(shapeModel.getId());
        if(!shapeCommonPropertyEntity.isPresent() || !linePropertyEntity.isPresent())
            return null;

        IMPGShapeCommonPropertyModel shapeCommonPropertyModel = new IMPGShapeCommonPropertyModel(shapeCommonPropertyEntity.get());
        IMPGLineModel model = new IMPGLineModel(shapeModel, shapeCommonPropertyModel, linePropertyEntity.get());


        return model;
    }

    public IMPGLineModel createEntity(IMPGLineModel impgLineModel) {
        // Create Rectangle Property
        IMPGShapeLinePropertyEntity entity = new IMPGShapeLinePropertyEntity(impgLineModel);
        entity = impgShapeLinePropertyEntityRepository.save(entity);

        return new IMPGLineModel(impgLineModel.getShape(), impgLineModel.getCommonProperty(), entity);
    }

    public IMPGLineModel updateEntity(Integer id, IMPGLineModel impgLineModel) {
        Optional<IMPGShapeLinePropertyEntity> optionalEntity = impgShapeLinePropertyEntityRepository.findIMPGShapeLinePropertyEntityByImpgShapeId(id);
        if(optionalEntity.isPresent()) {
            IMPGShapeLinePropertyEntity entity = optionalEntity.get();

            entity = impgShapeLinePropertyEntityRepository.save(new IMPGShapeLinePropertyEntity(entity.getImpg_shape_line_property_id(), impgLineModel));
            log.info("test : " + entity);
            return new IMPGLineModel(impgLineModel.getShape(), impgLineModel.getCommonProperty(), entity);
        } else {
            log.error("Line not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {

        Optional<IMPGShapeLinePropertyEntity> optionalEntity = impgShapeLinePropertyEntityRepository.findIMPGShapeLinePropertyEntityByImpgShapeId(id);
        if (optionalEntity.isPresent()) {
            impgShapeLinePropertyEntityRepository.deleteById(optionalEntity.get().getImpg_shape_line_property_id());
            return true;
        } else {
            log.error("Line not found with id: {}", id);
            return false;
        }
    }
}
