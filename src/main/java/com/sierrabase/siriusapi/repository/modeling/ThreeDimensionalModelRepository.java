package com.sierrabase.siriusapi.repository.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThreeDimensionalModelRepository extends JpaRepository<ThreeDimensionalModelEntity, Integer> {
    @Query("SELECT tdm FROM ThreeDimensionalModelEntity tdm where tdm.facility_id=:facilityId")
    List<ThreeDimensionalModelEntity> findAllByFacilityId(Integer facilityId);
}
