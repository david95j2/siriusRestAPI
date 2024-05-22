package com.sierrabase.siriusapi.model.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ThreeDimensionalFacilityModel {
    private int id;
    private int threeDimensionalModelingId;
    private int albumId;
    private String threeDimensionalFacilityUrl;
    private int type;
    private String typeName;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public ThreeDimensionalFacilityModel(final ThreeDimensionalFacilityEntity entity) {
        setId(entity.getThree_dimensional_facility_id());
        setThreeDimensionalModelingId(entity.getThree_dimensional_modeling_id());
        setAlbumId(entity.getAlbum_id());
        setThreeDimensionalFacilityUrl(entity.getThree_dimensional_facility_url());
        setType(entity.getType());
        setTypeName(entity.getType_name());
        setCreatedDatetime(entity.getCreated_datetime());
    }
}
