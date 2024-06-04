package com.sierrabase.siriusapi.repository.analysis;




import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;
import com.sierrabase.siriusapi.entity.analysis.AnalysisElevationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalysisElevationEntityRepository extends JpaRepository<AnalysisElevationEntity, Integer> {

    @Query("select ae from AnalysisElevationEntity ae where ae.analysis_id =:analysisId")
    List<AnalysisElevationEntity> findByAnalysisId(Integer analysisId);
    @Query("select ae from AnalysisElevationEntity ae where ae.three_dimensional_facility_roi_id =:roiId")
    List<AnalysisElevationEntity> findAllByRoiId(Integer roiId);
}
