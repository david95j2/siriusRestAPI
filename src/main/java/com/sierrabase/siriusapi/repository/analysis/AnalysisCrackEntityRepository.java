package com.sierrabase.siriusapi.repository.analysis;



import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnalysisCrackEntityRepository extends JpaRepository<AnalysisCrackEntity, Integer> {

    @Query("select ac from AnalysisCrackEntity ac where ac.analysis_id =:analysisId")
    List<AnalysisCrackEntity> findByAnalysisId(Integer analysisId);
}
