package com.sierrabase.siriusapi.repository;

import com.sierrabase.siriusapi.entity.FacilityMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacilityMapEntityRepository extends JpaRepository<FacilityMapEntity, Integer> {
    @Query("SELECT fme FROM FacilityMapEntity fme WHERE fme.facility_id = :facilityId")
    List<FacilityMapEntity> findAllByFacilityId(Integer facilityId);
}
