package com.sierrabase.siriusapi.model.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityRoiEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ThreeDimensionalFacilityRoiModel {
    private int id;
    private int threeDimensionalFacilityId;
    private int rotation;
    private double minHeight;
    private double maxHeight;
    private double minWidth;
    private double maxWidth;
    private double minDepth;
    private double maxDepth;
    private Boolean vertical;
    private String name;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public ThreeDimensionalFacilityRoiModel(final ThreeDimensionalFacilityRoiEntity entity) {
        setId(entity.getThree_dimensional_facility_roi_id());
        setThreeDimensionalFacilityId(entity.getThree_dimensional_facility_id());
        setRotation(entity.getRotation());
        setMinHeight(entity.getMin_height());
        setMaxHeight(entity.getMax_height());
        setMinWidth(entity.getMin_width());
        setMaxWidth(entity.getMax_width());
        setMinDepth(entity.getMin_depth());
        setMaxDepth(entity.getMax_depth());
        setVertical(entity.getVertical());
        setName(entity.getName());
        setCreatedDatetime(entity.getCreated_datetime());
    }


    public String rotationToString() {
        return String.valueOf(rotation);
    }

    public String minHeightToString() {
        return String.valueOf(minHeight);
    }

    public String maxHeightToString() {
        return String.valueOf(maxHeight);
    }

    public String minWidthToString() {
        return String.valueOf(minWidth);
    }

    public String maxWidthToString() {
        return String.valueOf(maxWidth);
    }

    public String minDepthToString() {
        return String.valueOf(minDepth);
    }

    public String maxDepthToString() {
        return String.valueOf(maxDepth);
    }

    public String verticalToString() {
        return String.valueOf(vertical);
    }

}
