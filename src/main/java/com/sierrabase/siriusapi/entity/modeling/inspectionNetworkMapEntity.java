package com.sierrabase.siriusapi.entity.modeling;

import com.sierrabase.siriusapi.model.modeling.InspectionNetworkMapModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="inspection_network_map")
public class inspectionNetworkMapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_network_map_id", unique = true, nullable = false)
    private int inspection_network_map_id;
    @Column(name = "three_dimensional_facility_id")
    private int three_dimensional_facility_id;
    @Column(name = "inspection_network_map_url")
    private String inspection_network_map_url;
    @Column(name = "status")
    private String status;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public inspectionNetworkMapEntity(final InspectionNetworkMapModel model) {
        setInspection_network_map_id(model.getId());
        setThree_dimensional_facility_id(model.getThreeDimensionalFacilityId());
        setInspection_network_map_url(model.getInspectionNetworkMapUrl());
        setStatus(model.getStatus());
        setCreated_datetime(model.getCreatedDatetime());
    }

    public inspectionNetworkMapEntity(final Integer id, final InspectionNetworkMapModel model) {
        this(model);
        setInspection_network_map_id(id);
    }
}
