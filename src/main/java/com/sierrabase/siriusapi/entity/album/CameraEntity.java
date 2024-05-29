package com.sierrabase.siriusapi.entity.album;

import com.sierrabase.siriusapi.model.album.CameraModel;
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
@Table(name="camera")
public class CameraEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camera_id", unique = true, nullable = false)
    private int camera_id;
    private String name;
    private String company;
    @Column(name = "model_name")
    private String model_name;
    @Column(name = "photo_width")
    private int photoWidth;
    @Column(name = "photo_height")
    private int photoHeight;
    @Column(name = "f_stop")
    private double f_stop;
    @Column(name = "exposure_time")
    private double exposure_time;
    @Column(name = "exposure_bias")
    private double exposure_bias;
    @Column(name = "focus_distance")
    private double focus_distance;
    @Column(name = "iso")
    private double iso;
    @Column(name = "horizontal_fov")
    private double horizontal_fov;
    @Column(name = "vertical_fov")
    private double vertical_fov;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;


    public CameraEntity (final CameraModel model) {
        setCamera_id(model.getId());
        setName(model.getName());
        setCompany(model.getCompany());
        setModel_name(model.getModel_name());
        setPhotoWidth(model.getPhotoWidth());
        setPhotoHeight(model.getPhotoHeight());
        setF_stop(model.getFStop());
        setExposure_time(model.getExposureTime());
        setExposure_bias(model.getExposureBias());
        setFocus_distance(model.getFocusDistance());
        setIso(model.getIso());
        setHorizontal_fov(model.getHorizontalFov());
        setVertical_fov(model.getVerticalFov());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public CameraEntity(final Integer id, final CameraModel model) {
        this(model);
        setCamera_id(id);
    }
}
