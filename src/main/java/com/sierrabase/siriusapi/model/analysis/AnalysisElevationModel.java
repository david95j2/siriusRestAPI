package com.sierrabase.siriusapi.model.analysis;

import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;
import com.sierrabase.siriusapi.entity.analysis.AnalysisElevationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalysisElevationModel {
    private int id;
    private int analysisId;
    private int threeDimensionalFacilityRoiId;
    private String elevationInfoPath;
    private int crackCount;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public AnalysisElevationModel(final AnalysisElevationEntity entity) {
        setId(entity.getAnalysis_elevation_id());
        setAnalysisId(entity.getAnalysis_id());
        setThreeDimensionalFacilityRoiId(entity.getThree_dimensional_facility_roi_id());
        setElevationInfoPath(entity.getElevation_info_path());
        setCrackCount(entity.getCrackCount());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }

    public AnalysisElevationModel(final int analysisId, final int roiId, String url, final int crackCount) {
        setAnalysisId(analysisId);
        setThreeDimensionalFacilityRoiId(roiId);
        setElevationInfoPath(url);
        setCrackCount(crackCount);
        setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
