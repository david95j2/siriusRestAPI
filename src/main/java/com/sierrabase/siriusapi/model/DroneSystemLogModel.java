package com.sierrabase.siriusapi.model;

import com.sierrabase.siriusapi.entity.DroneSystemLogEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class DroneSystemLogModel {
    private int droneSystemLogId;
    private int droneId;
    private int droneSystemServerity;
    private String droneSystemMessage;
    private ZonedDateTime created_datetime;

    public DroneSystemLogModel(final DroneSystemLogEntity entity) {
        setDroneSystemLogId(entity.getDroneSystemLogId());
        setDroneId(entity.getDroneId());
        setDroneSystemServerity(entity.getDroneSystemServerity());
        setDroneSystemMessage(entity.getDroneSystemMessage());
        setCreated_datetime(entity.getCreated_datetime());
    }
}
