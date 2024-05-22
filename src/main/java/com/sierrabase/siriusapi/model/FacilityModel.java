package com.sierrabase.siriusapi.model;

import com.sierrabase.siriusapi.entity.FacilityEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class FacilityModel {
    private int id;
    private String name;
    private double lat;
    private double lon;
    private String description;
    private String thumbnailUrl;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public FacilityModel(final FacilityEntity entity) {
        setId(entity.getFacility_id());
        setName(entity.getFacility_name());
        setLat(entity.getFacility_lat());
        setLon(entity.getFacility_lon());
        setDescription(entity.getFacility_description());
        setThumbnailUrl(entity.getFacility_thumbnail_url());
        setCreatedDatetime(entity.getCreated_datetime());
//        setWrDatetime(entity.getWr_datetime());
    }
}
