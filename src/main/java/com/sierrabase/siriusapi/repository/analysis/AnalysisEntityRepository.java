package com.sierrabase.siriusapi.repository.analysis;



import com.sierrabase.siriusapi.entity.analysis.AnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnalysisEntityRepository extends JpaRepository<AnalysisEntity, Integer> {
    @Query("select a from AnalysisEntity a where a.album_id =:albumId")
    List<AnalysisEntity> findByAlbumId(Integer albumId);

    @Query("select a from AnalysisEntity a where a.analysis_id =:analysisId and a.album_id =:albumId")
    Optional<AnalysisEntity> findByIdAndAlbumId(Integer analysisId, Integer albumId);
}
