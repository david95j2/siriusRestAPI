package com.sierrabase.siriusapi.entity;

import com.sierrabase.siriusapi.model.DroneTypeModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="drone_type")
public class DroneTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drone_type_id", unique = true, nullable = false)
    private int drone_type_id;
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "drone_type_name")
    private String drone_type_name;
    @Column(name = "drone_type_dimension_x")
    private double drone_type_dimension_x;
    @Column(name = "drone_type_dimension_y")
    private double drone_type_dimension_y;
    @Column(name = "drone_type_dimension_z")
    private double drone_type_dimension_z;
    @Column(name = "drone_type_min_voltage")
    private double drone_type_min_voltage;
    @Column(name = "drone_type_max_voltage")
    private double drone_type_max_voltage;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public DroneTypeEntity (final DroneTypeModel model) {
        setDrone_type_id(model.getId());
        setDrone_type_name(model.getName());
        setDrone_type_dimension_x(model.getDimensionX());
        setDrone_type_dimension_y(model.getDimensionY());
        setDrone_type_dimension_z(model.getDimensionZ());
        setDrone_type_min_voltage(model.getMinVoltage());
        setDrone_type_max_voltage(model.getMaxVoltage());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public DroneTypeEntity (final Integer id, final DroneTypeModel model){
        this(model);
        setDrone_type_id(id);
    }
}
