package com.sierrabase.siriusapi.entity.inspection.camera.property;

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
@Table(name="imp_wps_camera_property")
public class IMPWpsCameraPropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imp_wps_camera_property_id", unique = true, nullable = false)
    private int imp_wps_camera_property_id;
    @Column(name = "imp_wps_id")
    private int imp_wps_id;
    @Column(name = "should_capture")
    private int should_capture;
    @Column(name = "has_gimbal_pitch_array")
    private int has_gimbal_pitch_array;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;
}
