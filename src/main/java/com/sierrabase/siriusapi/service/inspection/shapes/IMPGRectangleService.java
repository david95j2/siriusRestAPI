package com.sierrabase.siriusapi.service.inspection.shapes;

import com.sierrabase.siriusapi.entity.FacilityEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCommonPropertyEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeRectanglePropertyEntity;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGRectangleModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeCommonPropertyEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeEntityRepository;
import com.sierrabase.siriusapi.repository.inspection.shape.IMPGShapeRectanglePropertyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IMPGRectangleService {
    @Autowired
    private IMPGShapeEntityRepository impgShapeEntityRepository;
    @Autowired
    private IMPGShapeCommonPropertyEntityRepository impgShapeCommonPropertyEntityRepository;
    @Autowired
    private IMPGShapeRectanglePropertyEntityRepository impgShapeRectanglePropertyEntityRepository;

    public IMPGRectangleModel getEntityById(Integer sId) {
        Optional<IMPGShapeEntity> shapeEntity = impgShapeEntityRepository.findById(sId);
        if(!shapeEntity.isPresent())
            return null;

        IMPGShapeModel shapeModel = new IMPGShapeModel(shapeEntity.get());
        Optional<IMPGShapeCommonPropertyEntity> shapeCommonPropertyEntity = impgShapeCommonPropertyEntityRepository.findIMPGShapeCommonPropertyEntityByImpgShapeId(shapeModel.getId());
        Optional<IMPGShapeRectanglePropertyEntity> rectanglePropertyEntity = impgShapeRectanglePropertyEntityRepository.findIMPGShapeRectanglePropertyEntityByImpgShapeId(shapeModel.getId());
        if(!shapeCommonPropertyEntity.isPresent() || !rectanglePropertyEntity.isPresent())
            return null;

        IMPGShapeCommonPropertyModel shapeCommonPropertyModel = new IMPGShapeCommonPropertyModel(shapeCommonPropertyEntity.get());
        IMPGRectangleModel model = new IMPGRectangleModel(shapeModel, shapeCommonPropertyModel, rectanglePropertyEntity.get());


        return model;
    }

    public IMPGRectangleModel createEntity(IMPGRectangleModel rectangleModel) {

        // Create Rectangle Property
        IMPGShapeRectanglePropertyEntity entity = new IMPGShapeRectanglePropertyEntity(rectangleModel);
        entity = impgShapeRectanglePropertyEntityRepository.save(entity);

        return new IMPGRectangleModel(rectangleModel.getShape(), rectangleModel.getCommonProperty(), entity);
    }

    public IMPGRectangleModel updateEntity(Integer id, IMPGRectangleModel impgRectangleModel) {
        Optional<IMPGShapeRectanglePropertyEntity> optionalEntity = impgShapeRectanglePropertyEntityRepository.findIMPGShapeRectanglePropertyEntityByImpgShapeId(id);
        if(optionalEntity.isPresent()) {
            IMPGShapeRectanglePropertyEntity entity = optionalEntity.get();

            entity = impgShapeRectanglePropertyEntityRepository.save(new IMPGShapeRectanglePropertyEntity(entity.getImpg_shape_rectangle_property_id(), impgRectangleModel));
            log.info("test : " + entity);
            return new IMPGRectangleModel(impgRectangleModel.getShape(), impgRectangleModel.getCommonProperty(), entity);
        } else {
            log.error("Rectangle not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {

        Optional<IMPGShapeRectanglePropertyEntity> optionalEntity = impgShapeRectanglePropertyEntityRepository.findIMPGShapeRectanglePropertyEntityByImpgShapeId(id);
        if (optionalEntity.isPresent()) {

            impgShapeRectanglePropertyEntityRepository.deleteById(optionalEntity.get().getImpg_shape_rectangle_property_id());
            return true;
        } else {
            log.error("Rectangle not found with id: {}", id);
            return false;
        }
    }
}
