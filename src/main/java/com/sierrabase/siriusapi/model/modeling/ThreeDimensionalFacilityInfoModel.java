package com.sierrabase.siriusapi.model.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityInfoEntity;
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
public class ThreeDimensionalFacilityInfoModel {
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

    public ThreeDimensionalFacilityInfoModel(final ThreeDimensionalFacilityInfoEntity entity) {
        setId(entity.getThree_dimensional_facility_info_id());
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
}
