package com.sierrabase.siriusapi.repository.mapping;

import com.sierrabase.siriusapi.entity.FacilityMapEntity;
import com.sierrabase.siriusapi.entity.mapping.MappingMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MappingMissionEntityRepository extends JpaRepository<MappingMissionEntity, Integer> {
    @Query("SELECT mme FROM MappingMissionEntity mme WHERE mme.facility_map_id = :facilityMapId")
    List<MappingMissionEntity> findAllByFacilityMapId(Integer facilityMapId);
}
