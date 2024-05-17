package com.sierrabase.siriusapi.model.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class IMPModel {
    private int id;
    private int facilityMapId;
    private String name;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPModel(final IMPEntity entity) {
        setId(entity.getImp_id());
        setFacilityMapId(entity.getFacility_map_id());
        setName(entity.getImp_name());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
