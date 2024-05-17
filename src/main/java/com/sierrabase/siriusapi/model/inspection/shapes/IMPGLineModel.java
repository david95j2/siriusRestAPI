package com.sierrabase.siriusapi.model.inspection.shapes;


import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeLinePropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IMPGLineModel {
    private IMPGShapeModel shape;
    private IMPGShapeCommonPropertyModel commonProperty;
    private int id;
    private double auto;
    private String direction;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPGLineModel(final IMPGShapeModel shapeModel, final IMPGShapeCommonPropertyModel shapeCommonPropertyModel, final IMPGShapeLinePropertyEntity entity) {
        shape = shapeModel;
        commonProperty = shapeCommonPropertyModel;
        setId(entity.getImpg_shape_line_property_id());
        setAuto(entity.getAuto());
        setDirection(entity.getDirection());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
