package com.sierrabase.siriusapi.entity.inspection;

import com.sierrabase.siriusapi.model.inspection.IMPWpsMissionCameraModel;
import com.sierrabase.siriusapi.model.inspection.IMPWpsModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="imp_wps_mission_camera")
public class IMPWpsMissionCameraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imp_wps_mission_camera_id", unique = true, nullable = false)
    private int imp_wps_mission_camera_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "imp_wps_id")
    private int imp_wps_id;
    @Column(name = "gimbal_pitch")
    private String gimbal_pitch;
    @Column(name = "should_capture")
    private Boolean should_capture;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPWpsMissionCameraEntity(final IMPWpsMissionCameraModel model) {
        setImp_wps_mission_camera_id(model.getId());
        setImp_id(model.getImpId());
        setImp_wps_id(model.getImpWpsId());
        if (model.getGimbalPitch() != null) {
            setGimbal_pitch(model.getGimbalPitch().stream().map(String::valueOf).toList().toString());
        }
        setShould_capture(model.getShouldCapture());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPWpsMissionCameraEntity(final Integer id, final IMPWpsMissionCameraModel model) {
        this(model);
        setImp_wps_id(id);
    }
}
