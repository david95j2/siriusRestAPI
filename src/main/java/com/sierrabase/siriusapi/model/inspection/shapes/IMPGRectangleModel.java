package com.sierrabase.siriusapi.model.inspection.shapes;


import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeRectanglePropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IMPGRectangleModel {
    private IMPGShapeModel shape;
    private IMPGShapeCommonPropertyModel commonProperty;
    private int id;
    private int direction;
    private double coeffsP1X;
    private double coeffsP1Y;
    private double coeffsP2X;
    private double coeffsP2Y;
    private double coeffsP3X;
    private double coeffsP3Y;
    private double coeffsP4X;
    private double coeffsP4Y;
    private double coeffsRot;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPGRectangleModel(final IMPGShapeModel shapeModel, final IMPGShapeCommonPropertyModel shapeCommonPropertyModel, final IMPGShapeRectanglePropertyEntity entity) {
        shape = shapeModel;
        commonProperty = shapeCommonPropertyModel;
        setId(entity.getImpg_shape_rectangle_property_id());
        setDirection(entity.getDirection());
        setCoeffsP1X(entity.getCoeffs_p1_x());
        setCoeffsP1Y(entity.getCoeffs_p1_y());
        setCoeffsP2X(entity.getCoeffs_p2_x());
        setCoeffsP2Y(entity.getCoeffs_p2_y());
        setCoeffsP3X(entity.getCoeffs_p3_x());
        setCoeffsP3Y(entity.getCoeffs_p3_y());
        setCoeffsP4X(entity.getCoeffs_p4_x());
        setCoeffsP4Y(entity.getCoeffs_p4_y());
        setCoeffsRot(entity.getCoeffs_rot());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
