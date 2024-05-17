package com.sierrabase.siriusapi.model.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeAbutmentPropertyEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IMPGAbutmentModel {
    private IMPGShapeModel shape;
    private IMPGShapeCommonPropertyModel commonProperty;
    private int id;
    private double cameraPitchInterval;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPGAbutmentModel(final IMPGShapeModel shapeModel, final IMPGShapeCommonPropertyModel shapeCommonPropertyModel, final IMPGShapeAbutmentPropertyEntity entity) {
        shape = shapeModel;
        commonProperty = shapeCommonPropertyModel;
        setId(entity.getImpg_shape_abutment_property_id());
        setCameraPitchInterval(entity.getCamera_pitch_interval());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
