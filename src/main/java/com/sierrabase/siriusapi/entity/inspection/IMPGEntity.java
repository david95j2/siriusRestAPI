package com.sierrabase.siriusapi.entity.inspection;

import com.sierrabase.siriusapi.model.inspection.IMPGModel;
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
@Table(name="impg")
public class IMPGEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impg_id", unique = true, nullable = false)
    private int impg_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "impg_seq")
    private int impg_seq;
    @Column(name = "impg_shape_type")
    private int impg_shape_type;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPGEntity(final IMPGModel model) {
        setImpg_id(model.getId());
        setImp_id(model.getImpId());
        setImpg_seq(model.getImpgSeq());
        setImpg_shape_type(model.getImpgShapeType());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPGEntity(final Integer id, IMPGModel model) {
        this(model);
        setImpg_id(id);
    }
}
