package com.sierrabase.siriusapi.entity.inspection.shape;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
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
@Table(name="impg_shape")
public class IMPGShapeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impg_shape_id", unique = true, nullable = false)
    private int impg_shape_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "impg_id")
    private int impg_id;
    @Column(name = "impg_shape_name")
    private String impg_shape_name;
    @Column(name = "impg_shape_type")
    private String impg_shape_type;
    @Column(name = "impg_shape_type_property")
    private int impg_shape_type_property;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPGShapeEntity(final IMPGShapeModel model) {
        setImpg_shape_id(model.getId());
        setImp_id(model.getImpId());
        setImpg_id(model.getImpgId());
        setImpg_shape_name(model.getName());
        setImpg_shape_type(model.getType());
        setImpg_shape_type_property(model.getTypeProperty());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPGShapeEntity(final Integer id, final IMPGShapeModel model) {
        this(model);
        setImpg_shape_id(id);
    }
}

