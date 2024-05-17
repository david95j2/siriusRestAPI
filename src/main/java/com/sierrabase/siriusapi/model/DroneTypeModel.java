package com.sierrabase.siriusapi.model;

import com.sierrabase.siriusapi.entity.DroneTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DroneTypeModel {
    private int id;
    private String name;
    private double dimensionX;
    private double dimensionY;
    private double dimensionZ;
    private double minVoltage;
    private double maxVoltage;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public DroneTypeModel(final DroneTypeEntity entity) {
        setId(entity.getDrone_type_id());
        setName(entity.getDrone_type_name());
        setDimensionX(entity.getDrone_type_dimension_x());
        setDimensionY(entity.getDrone_type_dimension_y());
        setDimensionZ(entity.getDrone_type_dimension_z());
        setMinVoltage(entity.getDrone_type_min_voltage());
        setMaxVoltage(entity.getDrone_type_max_voltage());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
