package com.sierrabase.siriusapi.model.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ThreeDimensionalModel {
    private int id;
    private int facilityId;
    private String threeDimensionalModelUrl;
    private String elevationStatus;
    private ZonedDateTime albumDatetime;
    private String albumName;
    private ZonedDateTime createdDatetime;

    public ThreeDimensionalModel(final ThreeDimensionalModelEntity entity) {
        setId(entity.getThree_dimensional_model_id());
        setFacilityId(entity.getFacility_id());
        setThreeDimensionalModelUrl(entity.getThree_dimensional_model_url());
        setElevationStatus(entity.getElevation_status());
        setAlbumDatetime(entity.getAlbum_datetime());
        setAlbumName(entity.getAlbumName());
        setCreatedDatetime(entity.getCreated_datetime());
    }
}
