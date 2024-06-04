package com.sierrabase.siriusapi.model.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ThreeDimensionalFacilityModel {
    private int id;
    private int facilityId;
    private String threeDimensionalFacilityUrl;
    private String elevationStatus;
    private ZonedDateTime albumDatetime;
    private String albumName;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public ThreeDimensionalFacilityModel(final ThreeDimensionalFacilityEntity entity) {
        setId(entity.getThree_dimensional_facility_id());
        setFacilityId(entity.getFacility_id());
        setThreeDimensionalFacilityUrl(entity.getThree_dimensional_facility_url());
        setElevationStatus(entity.getElevation_status());
        setAlbumDatetime(entity.getAlbum_datetime());
        setAlbumName(entity.getAlbum_name());
        setCreatedDatetime(entity.getCreated_datetime());
    }

    public ThreeDimensionalFacilityModel(final Integer facilityId, final AlbumModel model) {
        setFacilityId(facilityId);
        setElevationStatus("Waiting");
        setAlbumDatetime(model.getCreatedDatetime());
        setAlbumName(model.getName());
        setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
