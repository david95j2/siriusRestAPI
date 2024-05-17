package com.sierrabase.siriusapi.model.mapping;

import com.sierrabase.siriusapi.entity.mapping.MappingMissionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class MappingMissionModel {
    private int id;
    private int facilityMapId;
    private String name;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public MappingMissionModel(final MappingMissionEntity entity) {
        setId(entity.getMapping_mission_id());
        setFacilityMapId(entity.getFacility_map_id());
        setName(entity.getMapping_mission_name());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
