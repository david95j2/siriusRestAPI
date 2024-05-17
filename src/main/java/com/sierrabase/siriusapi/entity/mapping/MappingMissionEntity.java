package com.sierrabase.siriusapi.entity.mapping;

import com.sierrabase.siriusapi.model.mapping.MappingMissionModel;
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
@Table(name="mapping_mission")
public class MappingMissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapping_mission_id", unique = true, nullable = false)
    private int mapping_mission_id;
    @Column(name = "facility_map_id")
    private int facility_map_id;
    @Column(name = "mapping_mission_name")
    private String mapping_mission_name;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public MappingMissionEntity(final MappingMissionModel model) {
        setMapping_mission_id(model.getId());
        setFacility_map_id(model.getFacilityMapId());
        setMapping_mission_name(model.getName());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public MappingMissionEntity(final Integer id, final MappingMissionModel model) {
        this(model);
        setMapping_mission_id(id);
    }
}