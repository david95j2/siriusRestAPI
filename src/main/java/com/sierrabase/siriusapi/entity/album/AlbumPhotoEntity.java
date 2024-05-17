package com.sierrabase.siriusapi.entity.album;

import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
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
@Table(name="album_photos")
public class AlbumPhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_photo_id", unique = true, nullable = false)
    private int album_photo_id;
    @Column(name = "album_id")
    private int album_id;
    @Column(name = "camera_id")
    private int camera_id;
    @Column(name = "album_photo_path")
    private String album_photo_path;
    @Column(name = "album_photo_thumbnails_path")
    private String album_photo_thumbnails_path;
    @Column(name = "album_photo_resized_path")
    private String album_photo_resized_path;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public AlbumPhotoEntity(final AlbumPhotoModel model) {
        setAlbum_photo_id(model.getId());
        setAlbum_id(model.getAlbumId());
        setCamera_id(model.getCameraId());
        setAlbum_photo_path(model.getAlbumPhotoPath());
        setAlbum_photo_thumbnails_path(model.getAlbumPhotoThumbnailsPath());
        setAlbum_photo_resized_path(model.getAlbumPhotoResizedPath());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public AlbumPhotoEntity(final Integer id, AlbumPhotoModel model) {
        this(model);
        setAlbum_photo_id(id);
    }
}
