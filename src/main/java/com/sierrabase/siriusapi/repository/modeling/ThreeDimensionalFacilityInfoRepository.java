package com.sierrabase.siriusapi.repository.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThreeDimensionalFacilityInfoRepository extends JpaRepository<ThreeDimensionalFacilityInfoEntity, Integer> {

    @Query("SELECT tdfi FROM ThreeDimensionalFacilityInfoEntity tdfi WHERE tdfi.three_dimensional_facility_id=:threeDimensionalFacilityId")
    List<ThreeDimensionalFacilityInfoEntity> findAllByThreeDimensionalFacilityId(Integer threeDimensionalFacilityId);
}
