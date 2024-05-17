package com.sierrabase.siriusapi.model.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPWpsMissionCameraEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class IMPWpsMissionCameraModel {
    private int id;
    private int impId;
    private int impWpsId;
    private List<Integer> gimbalPitch;
    private Boolean shouldCapture;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPWpsMissionCameraModel(final IMPWpsMissionCameraEntity entity) {
        setId(entity.getImp_wps_mission_camera_id());
        setImpId(entity.getImp_id());
        setImpWpsId(entity.getImp_wps_id());
        if (entity.getGimbal_pitch() != null) {
            String trimmedInput = entity.getGimbal_pitch().substring(1, entity.getGimbal_pitch().length() - 1);
            String[] stringNumbers = trimmedInput.split(", ");
            List<Integer> numbers = new ArrayList<>();
            for (String numberStr : stringNumbers) {
                numbers.add(Integer.parseInt(numberStr));
            }
            setGimbalPitch(numbers);

        }
        setShouldCapture(entity.getShould_capture());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
