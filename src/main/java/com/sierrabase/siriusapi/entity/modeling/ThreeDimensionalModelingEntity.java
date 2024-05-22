package com.sierrabase.siriusapi.entity.modeling;

import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModelingModel;
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
@Table(name="three_dimensional_modeling")
public class ThreeDimensionalModelingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "three_dimensional_modeling_id", unique = true, nullable = false)
    private int three_dimensional_modeling_id;
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

    public ThreeDimensionalModelingEntity(final ThreeDimensionalModelingModel model) {
        setThree_dimensional_modeling_id(model.getThreeDimensionalModelingId());
        setAlbum_id(model.getAlbumId());
        setName(model.getName());
        setTable_name(model.getTableName());
        setType(model.getType());
        setType_name(model.getTypeName());
        setStatus(model.getStatus());
        setCreated_datetime(model.getCreatedDatetime());
    }

    public ThreeDimensionalModelingEntity(final Integer id, final ThreeDimensionalModelingModel model) {
        this(model);
        setThree_dimensional_modeling_id(id);
    }
}
