package com.sierrabase.siriusapi.repository.album;


import com.sierrabase.siriusapi.entity.album.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface AlbumEntityRepository extends JpaRepository<AlbumEntity, Integer> {
    @Query("select a from AlbumEntity a where a.facility_map_id = :facility_map_id")
    List<AlbumEntity> findAllByFacilityMapId(Integer facility_map_id);

    @Query("select a from AlbumEntity a where a.facility_id = :facility_id")
    List<AlbumEntity> findAllByFacilityId(Integer facility_id);

    @Query("select a from AlbumEntity a where a.facility_id = :facility_id and a.facility_map_id = :facility_map_id")
    List<AlbumEntity> findAllByFacilityIdAndFacilityMapId(Integer facility_id, Integer facility_map_id);


}
