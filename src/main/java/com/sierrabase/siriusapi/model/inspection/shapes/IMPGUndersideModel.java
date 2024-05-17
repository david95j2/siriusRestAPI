package com.sierrabase.siriusapi.model.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeUndersidePropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IMPGUndersideModel {
    private IMPGShapeModel shape;
    private IMPGShapeCommonPropertyModel commonProperty;
    private int id;
    private double auto;
    private double direction;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPGUndersideModel(final IMPGShapeModel shapeModel, final IMPGShapeCommonPropertyModel shapeCommonPropertyModel, final IMPGShapeUndersidePropertyEntity entity) {
        shape = shapeModel;
        commonProperty = shapeCommonPropertyModel;
        setId(entity.getImpg_shape_underside_property_id());
        setAuto(entity.getBottom_auto());
        setDirection(entity.getBottom_direction());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
