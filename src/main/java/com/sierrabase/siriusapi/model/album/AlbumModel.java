package com.sierrabase.siriusapi.model.album;

import com.sierrabase.siriusapi.entity.album.AlbumEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AlbumModel {
    private int id;
    private int facilityId;
    private int facilityMapId;
    private int cameraId;
    private String name;
    private int photoCount;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public AlbumModel(final AlbumEntity entity) {
        setId(entity.getAlbum_id());
        setFacilityId(entity.getFacility_id());
        setFacilityMapId(entity.getFacility_map_id());
        setCameraId(entity.getCamera_id());
        setName(entity.getName());
        setPhotoCount(entity.getPhoto_count());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }
}
