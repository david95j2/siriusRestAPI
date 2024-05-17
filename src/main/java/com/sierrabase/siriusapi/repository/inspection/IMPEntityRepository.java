package com.sierrabase.siriusapi.repository.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPEntity;
import com.sierrabase.siriusapi.entity.mapping.MappingMissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMPEntityRepository extends JpaRepository<IMPEntity, Integer> {
    @Query("SELECT impe FROM IMPEntity impe WHERE impe.facility_map_id = :facilityMapId")
    List<IMPEntity> findAllByFacilityMapId(int facilityMapId);
}
