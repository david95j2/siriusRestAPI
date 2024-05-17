package com.sierrabase.siriusapi.entity.inspection.shape.property;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGAbutmentModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGLineModel;
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
@Table(name="impg_shape_line_property")
public class IMPGShapeLinePropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impg_shape_line_property_id", unique = true, nullable = false)
    private int impg_shape_line_property_id;
    @Column(name = "impg_shape_id")
    private int impg_shape_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "impg_id")
    private int impg_id;
    @Column(name = "impg_shape_type_property")
    private int impg_shape_type_property;
    @Column(name = "auto")
    private double auto;
    @Column(name = "direction")
    private String direction;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPGShapeLinePropertyEntity (final IMPGLineModel model) {
        setImpg_shape_line_property_id(model.getId());
        setImp_id(model.getShape().getImpId());
        setImpg_id(model.getShape().getImpgId());
        setImpg_shape_id(model.getShape().getId());
        setImpg_shape_type_property(model.getShape().getTypeProperty());
        setAuto(model.getAuto());
        setDirection(model.getDirection());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }


    public IMPGShapeLinePropertyEntity(final Integer id, final IMPGLineModel model) {
        this(model);
        setImpg_shape_line_property_id(id);
    }
}
