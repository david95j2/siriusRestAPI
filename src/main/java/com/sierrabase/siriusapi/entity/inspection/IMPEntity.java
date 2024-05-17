package com.sierrabase.siriusapi.entity.inspection;

import com.sierrabase.siriusapi.model.inspection.IMPModel;
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
@Table(name="imp")
public class IMPEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imp_id", unique = true, nullable = false)
    private int imp_id;
    @Column(name = "facility_map_id")
    private int facility_map_id;
    @Column(name = "imp_name")
    private String imp_name;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public IMPEntity(final IMPModel model) {
        setImp_id(model.getId());
        setFacility_map_id(model.getFacilityMapId());
        setImp_name(model.getName());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public IMPEntity(Integer id, final IMPModel model) {
        this(model);
        setImp_id(id);
    }
}
