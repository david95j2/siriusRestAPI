package com.sierrabase.siriusapi.repository.modeling;

import com.sierrabase.siriusapi.entity.album.AlbumEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalFacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ThreeDimensionalFacilityRepository extends JpaRepository<ThreeDimensionalFacilityEntity, Integer> {
    @Query("SELECT tdf FROM ThreeDimensionalFacilityEntity tdf where tdf.facility_id=:facilityId")
    List<ThreeDimensionalFacilityEntity> findAllByFacilityId(Integer facilityId);

    @Query("SELECT album FROM AlbumEntity album WHERE album.created_datetime=:albumDatetime")
    Optional<AlbumEntity> findByAlbumDateTime(ZonedDateTime albumDatetime);
}
