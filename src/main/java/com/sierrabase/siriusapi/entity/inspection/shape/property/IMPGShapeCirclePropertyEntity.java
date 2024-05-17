package com.sierrabase.siriusapi.entity.inspection.shape.property;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGCircleModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGRectangleModel;
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
@Table(name="impg_shape_circle_property")
public class IMPGShapeCirclePropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impg_shape_circle_property_id", unique = true, nullable = false)
    private int impg_shape_cirlce_property_id;
    @Column(name = "impg_shape_id")
    private int impg_shape_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "impg_id")
    private int impg_id;
    @Column(name = "impg_shape_type_property")
    private int impg_shape_type_property;
    @Column(name = "direction")
    private double direction;
    @Column(name = "rotate")
    private double rotate;
    @Column(name = "start_angle")
    private double start_angle;
    @Column(name = "coeffs_x")
    private double coeffs_x;
    @Column(name = "coeffs_y")
    private double coeffs_y;
    @Column(name = "coeffs_radius")
    private double coeffs_radius;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPGShapeCirclePropertyEntity(final IMPGCircleModel model) {
        setImpg_shape_cirlce_property_id(model.getId());
        setImp_id(model.getShape().getImpId());
        setImpg_id(model.getShape().getImpgId());
        setImpg_shape_id(model.getShape().getId());
        setImpg_shape_type_property(model.getShape().getTypeProperty());
        setDirection(model.getDirection());
        setRotate(model.getRotate());
        setStart_angle(model.getStartAngle());
        setCoeffs_x(model.getCoeffsX());
        setCoeffs_y(model.getCoeffsY());
        setCoeffs_radius(model.getCoeffsRadius());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPGShapeCirclePropertyEntity(final Integer id, final IMPGCircleModel model) {
        this(model);
        setImpg_shape_cirlce_property_id(id);
    }
}

