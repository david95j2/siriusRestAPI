package com.sierrabase.siriusapi.model;

import com.sierrabase.siriusapi.entity.FacilityEntity;
import com.sierrabase.siriusapi.entity.FacilityMapEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class FacilityMapModel {
    private int id;
    private int facilityId;
    private String name;
    private String url;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public FacilityMapModel(final FacilityMapEntity entity) {
        setId(entity.getFacility_map_id());
        setFacilityId(entity.getFacility_id());
        setName(entity.getFacility_map_name());
        setUrl(entity.getFacility_map_url());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
