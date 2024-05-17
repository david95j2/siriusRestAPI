package com.sierrabase.siriusapi.entity.analysis;

import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
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
@Table(name="analysis_crack")
public class AnalysisCrackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_crack_id", unique = true, nullable = false)
    private int analysis_crack_id;
    @Column(name = "analysis_id")
    private int analysis_id;
    @Column(name = "album_id")
    private int album_id;
    @Column(name = "analysis_type")
    private int analysis_type;
    @Column(name = "cracks_info_path")
    private String cracksInfoPath;
    @Column(name = "crack_count")
    private int crackCount;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public AnalysisCrackEntity(final AnalysisCrackModel model) {
        setAnalysis_crack_id(model.getId());
        setAnalysis_id(model.getAnalysisId());
        setAlbum_id(model.getAlbumId());
        setAnalysis_type(model.getAnalysisType());
        setCracksInfoPath(model.getCracksInfoPath());
        setCrackCount(model.getCrackCount());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public AnalysisCrackEntity(final Integer id, final AnalysisCrackModel model) {
        this(model);
        setAnalysis_crack_id(id);
    }
}
