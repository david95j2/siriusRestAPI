package com.sierrabase.siriusapi.entity;

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
@Table(name="facility")
public class FacilityEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id", unique = true, nullable = false)
    private int facility_id;
    @Column(name = "facility_name")
    private String facility_name;
    @Column(name = "facility_lat")
    private double facility_lat;
    @Column(name = "facility_lon")
    private double facility_lon;
    @Column(name = "facility_description")
    private String facility_description;
    @Column(name = "facility_thumbnail_url")
    private String facility_thumbnail_url;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public FacilityEntity(final FacilityModel model) {
        setFacility_id(model.getId());
        setFacility_name(model.getName());
        setFacility_lat(model.getLat());
        setFacility_lon(model.getLon());
        setFacility_description(model.getDescription());
        setFacility_thumbnail_url(model.getThumbnailUrl());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public FacilityEntity(final Integer id, final FacilityModel model) {
        this(model);
        setFacility_id(id);
    }
}