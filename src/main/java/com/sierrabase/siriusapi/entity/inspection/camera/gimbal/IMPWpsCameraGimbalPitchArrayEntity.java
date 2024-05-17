package com.sierrabase.siriusapi.entity.inspection.camera.gimbal;

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
@Table(name="imp_wps_camera_gimbal_pitch_array")
public class IMPWpsCameraGimbalPitchArrayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imp_wps_camera_property_gpa_id", unique = true, nullable = false)
    private int imp_wps_camera_property_gpa_id;
    @Column(name = "imp_wps_camera_property_id")
    private int imp_wps_camera_property_id;
    @Column(name = "array_seq")
    private int array_seq;
    @Column(name = "pitch")
    private float pitch;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;
}
