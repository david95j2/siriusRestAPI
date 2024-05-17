package com.sierrabase.siriusapi.model.album;

import com.sierrabase.siriusapi.entity.album.AlbumEntity;
import com.sierrabase.siriusapi.entity.album.AlbumPhotoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlbumPhotoModel {
    private int id;
    private int albumId;
    private int cameraId;
    private String albumPhotoPath;
    private String albumPhotoThumbnailsPath;
    private String albumPhotoResizedPath;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public AlbumPhotoModel(final AlbumPhotoEntity entity) {
        setId(entity.getAlbum_photo_id());
        setAlbumId(entity.getAlbum_id());
        setCameraId(entity.getCamera_id());
        setAlbumPhotoPath(entity.getAlbum_photo_path());
        setAlbumPhotoThumbnailsPath(entity.getAlbum_photo_thumbnails_path());
        setAlbumPhotoResizedPath(entity.getAlbum_photo_resized_path());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }

    public AlbumPhotoModel(final Integer albumId, final Integer cameraId, final String path) {
        setAlbumId(albumId);
        setCameraId(cameraId);
        setAlbumPhotoPath(path);
        setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
