package com.sierrabase.siriusapi.entity.inspection;

import com.sierrabase.siriusapi.model.inspection.IMPWpsModel;
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
@Table(name="imp_wps")
public class IMPWpsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imp_wps_id", unique = true, nullable = false)
    private int imp_wps_id;
    @Column(name = "imp_id")
    private int imp_id;
    @Column(name = "impg_id")
    private int impg_id;
    @Column(name = "no")
    private int no;
    @Column(name = "x")
    private double x;
    @Column(name = "y")
    private double y;
    @Column(name = "z")
    private double z;
    @Column(name = "yaw")
    private double yaw;
    @Column(name = "pitch")
    private double pitch;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPWpsEntity(final IMPWpsModel model) {
        setImp_wps_id(model.getId());
        setImp_id(model.getImpId());
        setImpg_id(model.getImpgId());
        setNo(model.getNo());
        setX(model.getX());
        setY(model.getY());
        setZ(model.getZ());
        setYaw(model.getYaw());
        setPitch(model.getPitch());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPWpsEntity(final Integer id, final IMPWpsModel model) {
        this(model);
        setImp_wps_id(id);
    }
}
