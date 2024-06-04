package com.sierrabase.siriusapi.repository.modeling;

import com.sierrabase.siriusapi.entity.modeling.inspectionNetworkMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InspectionNetworkMapEntityRepository extends JpaRepository<inspectionNetworkMapEntity, Integer> {
    @Query("SELECT inm FROM inspectionNetworkMapEntity inm where inm.three_dimensional_facility_id=:tdmId")
    List<inspectionNetworkMapEntity> findAllByThreeDimensionalModelId(Integer tdmId);
}
