package com.sierrabase.siriusapi.repository.album;


import com.sierrabase.siriusapi.entity.album.AlbumEntity;
import com.sierrabase.siriusapi.entity.album.AlbumPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlbumPhotoEntityRepository extends JpaRepository<AlbumPhotoEntity, Integer> {

    @Query("select ap from AlbumPhotoEntity ap where ap.album_id =:album_id")
    List<AlbumPhotoEntity> findByAlbumId(Integer album_id);


    @Query("select ap from AlbumPhotoEntity ap where ap.album_photo_path LIKE %:name")
    Optional<AlbumPhotoEntity> findByName(String name);
}
