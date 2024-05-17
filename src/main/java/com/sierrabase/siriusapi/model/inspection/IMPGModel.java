package com.sierrabase.siriusapi.model.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPGEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class IMPGModel {
    private int id;
    private int impId;
    private int impgSeq;
    private int impgShapeType;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPGModel(final IMPGEntity entity) {
        setId(entity.getImpg_id());
        setImpId(entity.getImp_id());
        setImpgSeq(entity.getImpg_seq());
        setImpgShapeType(entity.getImpg_shape_type());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
