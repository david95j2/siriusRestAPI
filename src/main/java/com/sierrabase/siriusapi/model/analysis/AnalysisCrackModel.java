package com.sierrabase.siriusapi.model.analysis;

import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;
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
public class AnalysisCrackModel {
    private int id;
    private int analysisId;
    private int albumId;
    private int photoId;
    private int analysisType;
    private String cracksInfoPath;
    private int crackCount;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public AnalysisCrackModel(final AnalysisCrackEntity entity) {
        setId(entity.getAnalysis_crack_id());
        setAnalysisId(entity.getAnalysis_id());
        setAlbumId(entity.getAlbum_id());
        setAnalysisType(entity.getAnalysis_type());
        setCracksInfoPath(entity.getCracksInfoPath());
        setCrackCount(entity.getCrackCount());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }

    public AnalysisCrackModel(final Integer photoId, final AnalysisCrackEntity entity) {
        setId(entity.getAnalysis_crack_id());
        setAnalysisId(entity.getAnalysis_id());
        setAlbumId(entity.getAlbum_id());
        setPhotoId(photoId);
        setAnalysisType(entity.getAnalysis_type());
        setCracksInfoPath(entity.getCracksInfoPath());
        setCrackCount(entity.getCrackCount());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }

    public AnalysisCrackModel(final int albumId,final int analysisId,final String path,final int crackCount) {
        setAnalysisId(analysisId);
        setAlbumId(albumId);
        setAnalysisType(1);
        setCracksInfoPath(path);
        setCrackCount(crackCount);
        setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
