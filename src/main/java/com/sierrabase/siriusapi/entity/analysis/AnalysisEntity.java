package com.sierrabase.siriusapi.entity.analysis;


import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
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
@Table(name="analysis")
public class AnalysisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id", unique = true, nullable = false)
    private int analysis_id;
    @Column(name = "album_id")
    private int album_id;
    @Column(name = "name")
    private String name;
    @Column(name = "table_name")
    private String table_name;
    @Column(name = "type")
    private int type;
    @Column(name = "type_name")
    private String type_name;
    @Column(name = "status")
    private String status;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public AnalysisEntity(final AnalysisModel model) {
        setAnalysis_id(model.getId());
        setAlbum_id(model.getAlbumId());
        setName(model.getName());
        setTable_name(model.getTableName());
        setType(model.getType());
        setType_name(model.getTypeName());
        setStatus(model.getStatus());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public AnalysisEntity(final Integer id, final AnalysisModel model) {
        this(model);
        setAnalysis_id(id);
    }
}
