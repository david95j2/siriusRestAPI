package com.sierrabase.siriusapi.repository.modeling;

import com.sierrabase.siriusapi.entity.modeling.NetworkOfCrackEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NetworkOfCrackRepository extends JpaRepository<NetworkOfCrackEntity, Integer> {
    @Query("SELECT noc FROM NetworkOfCrackEntity noc where noc.three_dimensional_model_id=:tdmId")
    List<NetworkOfCrackEntity> findAllByThreeDimensionalModelId(Integer tdmId);
}
