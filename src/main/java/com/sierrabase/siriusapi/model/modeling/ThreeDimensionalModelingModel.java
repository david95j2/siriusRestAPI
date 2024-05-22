package com.sierrabase.siriusapi.model.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ThreeDimensionalModelingModel {
    private int threeDimensionalModelingId;
    private int albumId;
    private String name;
    private String tableName;
    private int type;
    private String typeName;
    private String status;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public ThreeDimensionalModelingModel(final ThreeDimensionalModelingEntity entity) {
        setThreeDimensionalModelingId(entity.getThree_dimensional_modeling_id());
        setAlbumId(entity.getAlbum_id());
        setName(entity.getName());
        setTableName(entity.getTable_name());
        setType(entity.getType());
        setTypeName(entity.getType_name());
        setStatus(entity.getStatus());
        setCreatedDatetime(entity.getCreated_datetime());
    }
}
