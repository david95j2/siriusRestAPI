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
    @Column(name = "three_dimensional_modeling_id")
    private int three_dimensional_modeling_id;
    @Column(name = "album_id")
    private int album_id;
    @Column(name = "three_dimensional_facility_url")
    private String three_dimensional_facility_url;
    @Column(name = "type")
    private int type;
    @Column(name = "type_name")
    private String type_name;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public ThreeDimensionalFacilityEntity(final ThreeDimensionalFacilityModel model) {
        setThree_dimensional_facility_id(model.getId());
        setThree_dimensional_modeling_id(model.getThreeDimensionalModelingId());
        setAlbum_id(model.getAlbumId());
        setThree_dimensional_facility_url(model.getThreeDimensionalFacilityUrl());
        setType(model.getType());
        setType_name(model.getTypeName());
        setCreated_datetime(model.getCreatedDatetime());
    }

    public ThreeDimensionalFacilityEntity(final Integer id, final ThreeDimensionalFacilityModel model) {
        this(model);
        setThree_dimensional_facility_id(id);
    }
}
