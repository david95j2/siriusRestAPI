package com.sierrabase.siriusapi.repository.modeling;

import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThreeDimensionalModelingRepository extends JpaRepository<ThreeDimensionalModelingEntity, Integer> {
    @Query("SELECT tdm FROM ThreeDimensionalModelingEntity tdm WHERE tdm.album_id =:albumId")
    List<ThreeDimensionalModelingEntity> findAllByAlbumId(Integer albumId);
}
