package com.sierrabase.siriusapi.entity.modeling;

import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModel;
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
@Table(name="three_dimensional_model")
public class ThreeDimensionalModelEntity implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "three_dimensional_model_id", unique = true, nullable = false)
    private int three_dimensional_model_id;
    @Column(name = "facility_id")
    private int facility_id;
    @Column(name = "three_dimensional_model_url")
    private String three_dimensional_model_url;
    @Column(name = "elevation_status")
    private String elevation_status;
    @Column(name = "album_datetime")
    private ZonedDateTime album_datetime;
    @Column(name = "album_name")
    private String albumName;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public ThreeDimensionalModelEntity(final ThreeDimensionalModel model) {
        setThree_dimensional_model_id(model.getId());
        setFacility_id(model.getFacilityId());
        setThree_dimensional_model_url(model.getThreeDimensionalModelUrl());
        setElevation_status(model.getElevationStatus());
        setAlbum_datetime(model.getAlbumDatetime());
        setAlbumName(model.getAlbumName());
        setCreated_datetime(model.getCreatedDatetime());
    }

    public ThreeDimensionalModelEntity(final Integer id, final ThreeDimensionalModel model) {
        this(model);
        setThree_dimensional_model_id(id);
    }
}
