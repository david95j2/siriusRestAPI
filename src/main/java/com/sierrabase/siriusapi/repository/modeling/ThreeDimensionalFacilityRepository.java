package com.sierrabase.siriusapi.repository.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThreeDimensionalFacilityRepository extends JpaRepository<ThreeDimensionalFacilityEntity, Integer> {
    @Query("SELECT tdf FROM ThreeDimensionalFacilityEntity tdf where tdf.album_id=:albumId")
    List<ThreeDimensionalFacilityEntity> findAllByAlbumId(Integer albumId);
}
