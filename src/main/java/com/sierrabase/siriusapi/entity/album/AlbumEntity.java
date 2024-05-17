package com.sierrabase.siriusapi.entity.album;

import com.sierrabase.siriusapi.model.album.AlbumModel;
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
@Table(name="album")
public class AlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id", unique = true, nullable = false)
    private int album_id;
    @Column(name = "facility_id")
    private int facility_id;
    @Column(name = "facility_map_id")
    private int facility_map_id;
    @Column(name = "camera_id")
    private int camera_id;
    private String name;
    @Column(name = "photo_count")
    private int photo_count;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public AlbumEntity(final AlbumModel model) {
        setAlbum_id(model.getId());
        setFacility_id(model.getFacilityId());
        setFacility_map_id(model.getFacilityMapId());
        setCamera_id(model.getCameraId());
        setName(model.getName());
        setPhoto_count(model.getPhotoCount());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public AlbumEntity(final Integer id, AlbumModel model) {
        this(model);
        setAlbum_id(id);
    }
}
