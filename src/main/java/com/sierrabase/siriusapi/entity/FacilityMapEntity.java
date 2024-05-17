package com.sierrabase.siriusapi.entity;

import com.sierrabase.siriusapi.model.FacilityMapModel;
import com.sierrabase.siriusapi.model.FacilityModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="facility_map")
public class FacilityMapEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_map_id", unique = true, nullable = false)
    private int facility_map_id;
    @Column(name = "facility_id")
    private int facility_id;
    @Column(name = "facility_map_name")
    private String facility_map_name;
    @Column(name = "facility_map_url")
    private String facility_map_url;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public FacilityMapEntity(final FacilityMapModel model) {
        setFacility_map_id(model.getId());
        setFacility_id(model.getFacilityId());
        setFacility_map_name(model.getName());
        setFacility_map_url(model.getUrl());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public FacilityMapEntity(final Integer id, final FacilityMapModel model) {
        this(model);
        setFacility_map_id(id);
    }
}