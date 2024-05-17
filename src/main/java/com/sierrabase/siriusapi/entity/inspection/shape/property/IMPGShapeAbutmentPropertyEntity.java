package com.sierrabase.siriusapi.entity.inspection.shape.property;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGAbutmentModel;
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
@Table(name="impg_shape_abutment_property")
public class IMPGShapeAbutmentPropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impg_shape_abutment_property_id", unique = true, nullable = false)
    private int impg_shape_abutment_property_id;
    @Column(name = "impg_shape_id")
    private int impg_shape_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "impg_id")
    private int impg_id;
    @Column(name = "impg_shape_type_property")
    private int impg_shape_type_property;
    @Column(name = "camera_pitch_interval")
    private double camera_pitch_interval;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPGShapeAbutmentPropertyEntity(final IMPGAbutmentModel model) {
        setImpg_shape_abutment_property_id(model.getId());
        setImp_id(model.getShape().getImpId());
        setImpg_id(model.getShape().getImpgId());
        setImpg_shape_id(model.getShape().getId());
        setImpg_shape_type_property(model.getShape().getTypeProperty());
        setCamera_pitch_interval(model.getCameraPitchInterval());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPGShapeAbutmentPropertyEntity(final Integer id, final IMPGAbutmentModel model) {
        this(model);
        setImpg_shape_abutment_property_id(id);
    }
}

