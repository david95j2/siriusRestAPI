package com.sierrabase.siriusapi.repository.mapping;

import com.sierrabase.siriusapi.entity.mapping.MappingMissionWaypointsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MappingMissionWaypointsEntityRepository extends JpaRepository<MappingMissionWaypointsEntity, Integer> {
    @Query("SELECT mmwe FROM MappingMissionWaypointsEntity mmwe WHERE mmwe.mapping_mission_id = :missionId")
    List<MappingMissionWaypointsEntity> findAllByMappingMissionId(Integer missionId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MappingMissionWaypointsEntity mmwe WHERE mmwe.mapping_mission_id = :missionId")
    Integer deleteAllByMappingMissionId(Integer missionId);
}
