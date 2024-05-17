package com.sierrabase.siriusapi.model.inspection.shapes;


import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCommonPropertyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IMPGShapeCommonPropertyModel {
    private int id;
    private int impShapeId;
    private int impId;
    private int impgId;
    private double fromWall;
    private double interval;
    private double heightInterval;
    private double lowerHeight;
    private double upperHeight;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPGShapeCommonPropertyModel(final IMPGShapeCommonPropertyEntity entity) {
        setId(entity.getImpg_shape_common_property_id());
        setImpShapeId(entity.getImpg_shape_id());
        setImpId(entity.getImp_id());
        setImpgId(entity.getImpg_id());
        setFromWall(entity.getFrom_wall());
        setInterval(entity.getInterval());
        setHeightInterval(entity.getHeight_interval());
        setLowerHeight(entity.getLower_height());
        setUpperHeight(entity.getUpper_height());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
