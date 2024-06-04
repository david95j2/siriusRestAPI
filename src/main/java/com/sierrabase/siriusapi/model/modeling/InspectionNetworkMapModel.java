package com.sierrabase.siriusapi.model.modeling;

import com.sierrabase.siriusapi.entity.modeling.inspectionNetworkMapEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InspectionNetworkMapModel {
    private int id;
    private int threeDimensionalFacilityId;
    private String inspectionNetworkMapUrl;
    private String status;
    private ZonedDateTime createdDatetime;

    public InspectionNetworkMapModel(final inspectionNetworkMapEntity entity) {
        setId(entity.getInspection_network_map_id());
        setThreeDimensionalFacilityId(entity.getThree_dimensional_facility_id());
        setInspectionNetworkMapUrl(entity.getInspection_network_map_url());
        setStatus(entity.getStatus());
        setCreatedDatetime(entity.getCreated_datetime());
    }
}
