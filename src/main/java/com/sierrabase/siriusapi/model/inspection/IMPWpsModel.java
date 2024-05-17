package com.sierrabase.siriusapi.model.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPEntity;
import com.sierrabase.siriusapi.entity.inspection.IMPWpsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class IMPWpsModel {
    private int id;
    private int impId;
    private int impgId;
    private int no;
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public IMPWpsModel(final IMPWpsEntity entity) {
        setId(entity.getImp_wps_id());
        setImpId(entity.getImp_id());
        setImpgId(entity.getImpg_id());
        setNo(entity.getNo());
        setX(entity.getX());
        setY(entity.getY());
        setZ(entity.getZ());
        setYaw(entity.getYaw());
        setPitch(entity.getPitch());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
