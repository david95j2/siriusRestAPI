package com.sierrabase.siriusapi.model.mapping;

import com.sierrabase.siriusapi.entity.mapping.MappingMissionWaypointsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class MappingMissionWaypointsModel {
    private int id;
    private int mappingMissionId;
    private int no;
    private double lat;
    private double lon;
    private double alt;
    private double waitTime;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public MappingMissionWaypointsModel(final MappingMissionWaypointsEntity entity) {
        setId(entity.getMapping_mission_waypoints_id());
        setMappingMissionId(entity.getMapping_mission_id());
        setNo(entity.getNo());
        setLat(entity.getLat());
        setLon(entity.getLon());
        setAlt(entity.getAlt());
        setWaitTime(entity.getWait_time());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}