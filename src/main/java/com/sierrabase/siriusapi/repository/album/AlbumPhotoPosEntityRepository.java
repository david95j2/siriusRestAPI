package com.sierrabase.siriusapi.repository.album;


import com.sierrabase.siriusapi.entity.album.AlbumPhotoEntity;
import com.sierrabase.siriusapi.entity.album.AlbumPhotoPosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumPhotoPosEntityRepository extends JpaRepository<AlbumPhotoPosEntity, Integer> {
    @Query("select aps from AlbumPhotoPosEntity aps where aps.album_photo_id =:albumPhotoId")
    Optional<AlbumPhotoPosEntity> findByAlbumPhotoId(Integer albumPhotoId);

    @Query("SELECT ap FROM AlbumPhotoEntity ap WHERE ap.album_photo_path LIKE %:pattern% and ap.album_id=:albumId")
    List<AlbumPhotoEntity> findByPattern(Integer albumId, String pattern);
}
