package com.sierrabase.siriusapi.model.inspection.shapes;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class IMPGShapeModel {
    private int id;
    private int impId;
    private int impgId;
    private String name;
    private String type;
    private int typeProperty;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPGShapeModel(final IMPGShapeEntity entity) {
        setId(entity.getImpg_shape_id());
        setImpId(entity.getImp_id());
        setImpgId(entity.getImpg_id());
        setName(entity.getImpg_shape_name());
        setType(entity.getImpg_shape_type());
        setTypeProperty(entity.getImpg_shape_type_property());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
