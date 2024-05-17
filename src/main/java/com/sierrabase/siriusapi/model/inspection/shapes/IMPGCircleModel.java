package com.sierrabase.siriusapi.model.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCirclePropertyEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IMPGCircleModel {
    private IMPGShapeModel shape;
    private IMPGShapeCommonPropertyModel commonProperty;
    private int id;
    private double direction;
    private double rotate;
    private double startAngle;
    private double coeffsX;
    private double coeffsY;
    private double coeffsRadius;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPGCircleModel(final IMPGShapeModel shapeModel, final IMPGShapeCommonPropertyModel shapeCommonPropertyModel, final IMPGShapeCirclePropertyEntity entity) {
        shape = shapeModel;
        commonProperty = shapeCommonPropertyModel;
        setId(entity.getImpg_shape_cirlce_property_id());
        setDirection(entity.getDirection());
        setRotate(entity.getRotate());
        setStartAngle(entity.getStart_angle());
        setCoeffsX(entity.getCoeffs_x());
        setCoeffsY(entity.getCoeffs_y());
        setCoeffsRadius(entity.getCoeffs_radius());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
