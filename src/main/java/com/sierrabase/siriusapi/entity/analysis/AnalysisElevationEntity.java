package com.sierrabase.siriusapi.entity.analysis;

import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisElevationModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="analysis_elevation")
public class AnalysisElevationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_elevation_id", unique = true, nullable = false)
    private int analysis_elevation_id;
    @Column(name = "analysis_id")
    private int analysis_id;
    @Column(name = "three_dimensional_facility_roi_id")
    private int three_dimensional_facility_roi_id;
    @Column(name = "elevation_info_path")
    private String elevation_info_path;
    @Column(name = "crack_count")
    private int crackCount;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public AnalysisElevationEntity(final AnalysisElevationModel model) {
        setAnalysis_elevation_id(model.getId());
        setAnalysis_id(model.getAnalysisId());
        setThree_dimensional_facility_roi_id(model.getThreeDimensionalFacilityRoiId());
        setElevation_info_path(model.getElevationInfoPath());
        setCrackCount(model.getCrackCount());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public AnalysisElevationEntity(final Integer id, final AnalysisElevationModel model) {
        this(model);
        setAnalysis_elevation_id(id);
    }
}
