package com.sierrabase.siriusapi.repository.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityRoiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThreeDimensionalFacilityInfoRepository extends JpaRepository<ThreeDimensionalFacilityRoiEntity, Integer> {

    @Query("SELECT tdfi FROM ThreeDimensionalFacilityRoiEntity tdfi WHERE tdfi.three_dimensional_facility_id=:threeDimensionalFacilityId")
    List<ThreeDimensionalFacilityRoiEntity> findAllByThreeDimensionalFacilityId(Integer threeDimensionalFacilityId);
}
