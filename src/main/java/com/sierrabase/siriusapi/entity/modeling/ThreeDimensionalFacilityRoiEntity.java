package com.sierrabase.siriusapi.entity.modeling;

import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityRoiModel;
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
@Table(name="three_dimensional_facility_roi")
public class ThreeDimensionalFacilityRoiEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "three_dimensional_facility_roi_id", unique = true, nullable = false)
    private int three_dimensional_facility_roi_id;
    @Column(name = "three_dimensional_facility_id")
    private int three_dimensional_facility_id;
    @Column(name = "rotation")
    private int rotation;
    @Column(name = "min_height")
    private double min_height;
    @Column(name = "max_height")
    private double max_height;
    @Column(name = "min_width")
    private double min_width;
    @Column(name = "max_width")
    private double max_width;
    @Column(name = "min_depth")
    private double min_depth;
    @Column(name = "max_depth")
    private double max_depth;
    @Column(name = "vertical")
    private Boolean vertical;
    @Column(name = "name")
    private String name;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public ThreeDimensionalFacilityRoiEntity(final ThreeDimensionalFacilityRoiModel model) {
        setThree_dimensional_facility_roi_id(model.getId());
        setThree_dimensional_facility_id(model.getThreeDimensionalFacilityId());
        setRotation(model.getRotation());
        setMin_height(model.getMinHeight());
        setMax_height(model.getMaxHeight());
        setMin_width(model.getMinWidth());
        setMax_width(model.getMaxWidth());
        setMin_depth(model.getMinDepth());
        setMax_depth(model.getMaxDepth());
        setVertical(model.getVertical());
        setName(model.getName());
        setCreated_datetime(model.getCreatedDatetime());
    }

    public ThreeDimensionalFacilityRoiEntity(final Integer id, final ThreeDimensionalFacilityRoiModel model) {
        this(model);
        setThree_dimensional_facility_roi_id(id);
    }
}
