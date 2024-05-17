package com.sierrabase.siriusapi.entity.inspection.shape.property;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.model.inspection.IMPModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGRectangleModel;
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
@Table(name="impg_shape_rectangle_property")
public class IMPGShapeRectanglePropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impg_shape_rectangle_property_id", unique = true, nullable = false)
    private int impg_shape_rectangle_property_id;
    @Column(name = "impg_shape_id")
    private int impg_shape_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "impg_id")
    private int impg_id;
    @Column(name = "impg_shape_type_property")
    private int impg_shape_type_property;
    @Column(name = "direction")
    private int direction;
    @Column(name = "coeffs_p1_x")
    private double coeffs_p1_x;
    @Column(name = "coeffs_p1_y")
    private double coeffs_p1_y;
    @Column(name = "coeffs_p2_x")
    private double coeffs_p2_x;
    @Column(name = "coeffs_p2_y")
    private double coeffs_p2_y;
    @Column(name = "coeffs_p3_x")
    private double coeffs_p3_x;
    @Column(name = "coeffs_p3_y")
    private double coeffs_p3_y;
    @Column(name = "coeffs_p4_x")
    private double coeffs_p4_x;
    @Column(name = "coeffs_p4_y")
    private double coeffs_p4_y;
    @Column(name = "coeffs_rot")
    private double coeffs_rot;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPGShapeRectanglePropertyEntity(final IMPGRectangleModel model) {
        setImpg_shape_rectangle_property_id(model.getId());
        setImp_id(model.getShape().getImpId());
        setImpg_id(model.getShape().getImpgId());
        setImpg_shape_id(model.getShape().getId());
        setImpg_shape_type_property(model.getShape().getTypeProperty());
        setDirection(model.getDirection());
        setCoeffs_p1_x(model.getCoeffsP1X());
        setCoeffs_p1_y(model.getCoeffsP1Y());
        setCoeffs_p2_x(model.getCoeffsP2X());
        setCoeffs_p2_y(model.getCoeffsP2Y());
        setCoeffs_p3_x(model.getCoeffsP3X());
        setCoeffs_p3_y(model.getCoeffsP3Y());
        setCoeffs_p4_x(model.getCoeffsP4X());
        setCoeffs_p4_y(model.getCoeffsP4Y());
        setCoeffs_rot(model.getCoeffsRot());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPGShapeRectanglePropertyEntity(final Integer id, final IMPGRectangleModel model) {
        this(model);
        setImpg_shape_rectangle_property_id(id);
    }
}
