package com.sierrabase.siriusapi.entity.mapping;

import com.sierrabase.siriusapi.model.mapping.MappingMissionWaypointsModel;
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
@Table(name="mapping_mission_waypoints")
public class MappingMissionWaypointsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_mission_waypoints_id", unique = true, nullable = false)
    private int mapping_mission_waypoints_id;
    @Column(name = "mapping_mission_id")
    private int mapping_mission_id;
    @Column(name = "no")
    private int no;
    @Column(name = "lat")
    private double lat;
    @Column(name = "lon")
    private double lon;
    @Column(name = "alt")
    private double alt;
    @Column(name = "wait_time")
    private double wait_time;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public MappingMissionWaypointsEntity(final MappingMissionWaypointsModel model) {
        setMapping_mission_waypoints_id(model.getId());
        setMapping_mission_id(model.getMappingMissionId());
        setNo(model.getNo());
        setLat(model.getLat());
        setLon(model.getLon());
        setAlt(model.getAlt());
        setWait_time(model.getWaitTime());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public MappingMissionWaypointsEntity(final Integer id, final MappingMissionWaypointsModel model) {
        this(model);
        setMapping_mission_waypoints_id(id);
    }
}
