package com.sierrabase.siriusapi.service.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeAbutmentPropertyEntity;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCommonPropertyEntity;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGAbutmentModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeAbutmentPropertyEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeCommonPropertyEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IMPGAbutmentService {
    @Autowired
    private IMPGShapeEntityRepository impgShapeEntityRepository;
    @Autowired
    private IMPGShapeCommonPropertyEntityRepository impgShapeCommonPropertyEntityRepository;
    @Autowired
    private IMPGShapeAbutmentPropertyEntityRepository impgShapeAbutmentPropertyEntityRepository;

    public IMPGAbutmentModel getEntityById(Integer sId) {
        Optional<IMPGShapeEntity> shapeEntity = impgShapeEntityRepository.findById(sId);
        if(!shapeEntity.isPresent())
            return null;

        IMPGShapeModel shapeModel = new IMPGShapeModel(shapeEntity.get());
        Optional<IMPGShapeCommonPropertyEntity> shapeCommonPropertyEntity = impgShapeCommonPropertyEntityRepository.findIMPGShapeCommonPropertyEntityByImpgShapeId(shapeModel.getId());
        Optional<IMPGShapeAbutmentPropertyEntity> abutmentPropertyEntity = impgShapeAbutmentPropertyEntityRepository.findIMPGShapeAbutmentPropertyEntityByImpgShapeId(shapeModel.getId());
        if(!shapeCommonPropertyEntity.isPresent() || !abutmentPropertyEntity.isPresent())
            return null;

        IMPGShapeCommonPropertyModel shapeCommonPropertyModel = new IMPGShapeCommonPropertyModel(shapeCommonPropertyEntity.get());
        IMPGAbutmentModel model = new IMPGAbutmentModel(shapeModel, shapeCommonPropertyModel, abutmentPropertyEntity.get());


        return model;
    }

    public IMPGAbutmentModel createEntity(IMPGAbutmentModel impgAbutmentModel) {

        // Create Rectangle Property
        IMPGShapeAbutmentPropertyEntity entity = new IMPGShapeAbutmentPropertyEntity(impgAbutmentModel);
        entity = impgShapeAbutmentPropertyEntityRepository.save(entity);

        return new IMPGAbutmentModel(impgAbutmentModel.getShape(), impgAbutmentModel.getCommonProperty(), entity);
    }

    public IMPGAbutmentModel updateEntity(Integer id, IMPGAbutmentModel impgAbutmentModel) {
        Optional<IMPGShapeAbutmentPropertyEntity> optionalEntity = impgShapeAbutmentPropertyEntityRepository.findIMPGShapeAbutmentPropertyEntityByImpgShapeId(id);
        if(optionalEntity.isPresent()) {
            IMPGShapeAbutmentPropertyEntity entity = optionalEntity.get();

            entity = impgShapeAbutmentPropertyEntityRepository.save(new IMPGShapeAbutmentPropertyEntity(entity.getImpg_shape_abutment_property_id(), impgAbutmentModel));
            log.info("test : " + entity);
            return new IMPGAbutmentModel(impgAbutmentModel.getShape(), impgAbutmentModel.getCommonProperty(), entity);
        } else {
            log.error("Abutment not found with id: {}", id);
            return null;
        }
    }
//
    public boolean deleteEntity(Integer id) {

        Optional<IMPGShapeAbutmentPropertyEntity> optionalEntity = impgShapeAbutmentPropertyEntityRepository.findIMPGShapeAbutmentPropertyEntityByImpgShapeId(id);
        if (optionalEntity.isPresent()) {
            impgShapeAbutmentPropertyEntityRepository.deleteById(optionalEntity.get().getImpg_shape_abutment_property_id());
            return true;
        } else {
            log.error("Abutment not found with id: {}", id);
            return false;
        }
    }
}
