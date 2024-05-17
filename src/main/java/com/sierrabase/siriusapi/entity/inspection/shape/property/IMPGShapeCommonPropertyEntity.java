package com.sierrabase.siriusapi.entity.inspection.shape.property;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
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
@Table(name="impg_shape_common_property")
public class IMPGShapeCommonPropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "impg_shape_common_property_id", unique = true, nullable = false)
    private int impg_shape_common_property_id;
    @Column(name = "impg_shape_id")
    private int impg_shape_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "impg_id")
    private int impg_id;
    @Column(name = "impg_shape_type_property")
    private int impg_shape_type_property;
    @Column(name = "from_wall")
    private double from_wall;
    @Column(name = "time_interval")
    private double interval;
    @Column(name = "height_interval")
    private double height_interval;
    @Column(name = "lower_height")
    private double lower_height;
    @Column(name = "upper_height")
    private double upper_height;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPGShapeCommonPropertyEntity(final IMPGShapeCommonPropertyModel model, final IMPGShapeModel shapeModel) {
        setImpg_shape_common_property_id(model.getId());
        setImp_id(shapeModel.getImpId());
        setImpg_id(shapeModel.getImpgId());
        setImpg_shape_id(shapeModel.getId());
        setImpg_shape_type_property(shapeModel.getTypeProperty());
        setFrom_wall(model.getFromWall());
        setInterval(model.getInterval());
        setHeight_interval(model.getHeightInterval());
        setLower_height(model.getLowerHeight());
        setUpper_height(model.getUpperHeight());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPGShapeCommonPropertyEntity(final Integer id, final IMPGShapeCommonPropertyModel model, final IMPGShapeModel shapeModel) {
        this(model, shapeModel);
        setImpg_shape_common_property_id(id);
    }
}
