package com.sierrabase.siriusapi.entity;

import com.sierrabase.siriusapi.model.DroneSystemLogModel;
import com.sierrabase.siriusapi.model.LoginAttemptModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="drone_system_log")
public class DroneSystemLogEntity implements Serializable {
    @Id
    @Column(name = "drone_system_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int droneSystemLogId;

    @Column(name = "drone_id")
    private int droneId;

    @Column(name = "drone_system_serverity")
    private int droneSystemServerity;

    @Column(name = "drone_system_message")
    private String droneSystemMessage;

    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime = ZonedDateTime.now();

    public DroneSystemLogEntity(final DroneSystemLogModel model) {
        setDroneSystemLogId(model.getDroneSystemLogId());
        setDroneId(model.getDroneId());
        setDroneSystemServerity(model.getDroneSystemSeverity());
        setDroneSystemMessage(model.getDroneSystemMessage());
    }
    public DroneSystemLogEntity(final Integer id, final DroneSystemLogModel model) {
        this(model);
        setDroneSystemLogId(id);
    }

}