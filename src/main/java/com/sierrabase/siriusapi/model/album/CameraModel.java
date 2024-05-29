package com.sierrabase.siriusapi.model.album;

import com.sierrabase.siriusapi.common.StringToDouble;
import com.sierrabase.siriusapi.entity.album.CameraEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CameraModel {
    private int id;
    private String name;
    private String company;
    private String model_name;
    private int photoWidth;
    private int photoHeight;
    private double fStop;
    private double exposureTime;
    private double exposureBias;
    private double focusDistance;
    private double iso;
    private double horizontalFov;
    private double verticalFov;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public CameraModel(final CameraEntity entity) {
        setId(entity.getCamera_id());
        setName(entity.getName());
        setCompany(entity.getCompany());
        setModel_name(entity.getModel_name());
        setPhotoWidth(entity.getPhotoWidth());
        setPhotoHeight(entity.getPhotoHeight());
        setFStop(entity.getF_stop());
        setExposureTime(entity.getExposure_time());
        setExposureBias(entity.getExposure_bias());
        setFocusDistance(entity.getFocus_distance());
        setIso(entity.getIso());
        setHorizontalFov(entity.getHorizontal_fov());
        setVerticalFov(entity.getVertical_fov());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }

    public CameraModel(Map<String, String> metadataMap) {
        setName(metadataMap.get("Make"));
        setCompany(metadataMap.get("Make"));
        setModel_name(metadataMap.get("Model"));
        setPhotoWidth(Integer.parseInt(metadataMap.get("Image Width")));
        setPhotoHeight(Integer.parseInt(metadataMap.get("Image Height")));
        setFStop(Double.parseDouble(metadataMap.get("F-Number")));
        setExposureTime(StringToDouble.convertExposureTimeToDouble(metadataMap.get("Exposure Time")));
        setExposureBias(Double.parseDouble(metadataMap.get("Exposure Bias Value")));
        setFocusDistance(Double.parseDouble(metadataMap.get("Focal Length")));
        setIso(Double.parseDouble(metadataMap.get("ISO Speed Ratings")));
        setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
