package com.sierrabase.siriusapi.entity.modeling;

import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="three_dimensional_facility")
public class ThreeDimensionalFacilityEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "three_dimensional_facility_id", unique = true, nullable = false)
    private int three_dimensional_facility_id;
    @Column(name = "facility_id")
    private int facility_id;
    @Column(name = "three_dimensional_facility_url")
    private String three_dimensional_facility_url;
    @Column(name = "elevation_status")
    private String elevation_status;
    @Column(name = "album_datetime")
    private ZonedDateTime album_datetime;
    @Column(name = "album_name")
    private String album_name;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public ThreeDimensionalFacilityEntity(final ThreeDimensionalFacilityModel model) {
        setThree_dimensional_facility_id(model.getId());
        setFacility_id(model.getFacilityId());
        setThree_dimensional_facility_url(model.getThreeDimensionalFacilityUrl());
        setElevation_status(model.getElevationStatus());
        setAlbum_datetime(model.getAlbumDatetime());
        setAlbum_name(model.getAlbumName());
        setCreated_datetime(model.getCreatedDatetime());
    }

    public ThreeDimensionalFacilityEntity(final Integer id, final ThreeDimensionalFacilityModel model) {
        this(model);
        setThree_dimensional_facility_id(id);
    }
}
