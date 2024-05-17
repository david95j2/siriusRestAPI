package com.sierrabase.siriusapi.model.album;

import com.sierrabase.siriusapi.common.StringToDouble;
import com.sierrabase.siriusapi.entity.album.AlbumPhotoEntity;
import com.sierrabase.siriusapi.entity.album.AlbumPhotoPosEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Slf4j
public class AlbumPhotoPosModel {
    private AlbumPhotoModel photo;
    private int id;
    private int albumPhotoId;
    private double x;
    private double y;
    private double z;
    private double roll;
    private double pitch;
    private double yaw;
    private double quaternionX;
    private double quaternionY;
    private double quaternionZ;
    private double quaternionW;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public AlbumPhotoPosModel(final AlbumPhotoModel albumPhotoModel, final AlbumPhotoPosEntity entity) {
        photo = albumPhotoModel;
        setId(entity.getAlbum_photo_pos_id());
        setAlbumPhotoId(albumPhotoModel.getId());
        setX(entity.getX());
        setY(entity.getY());
        setZ(entity.getZ());
        setRoll(entity.getRoll());
        setPitch(entity.getPitch());
        setYaw(entity.getYaw());
        setQuaternionX(entity.getQuaternionX());
        setQuaternionY(entity.getQuaternionY());
        setQuaternionZ(entity.getQuaternionZ());
        setQuaternionW(entity.getQuaternionW());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }

    public AlbumPhotoPosModel(final Integer albumPhotoId, final String fileName, final AlbumPhotoModel albumPhotoModel) {
        String[] posInfo = fileName.split("_");
        log.info(fileName);
        setAlbumPhotoId(albumPhotoId);
        photo = albumPhotoModel;
        setX(Double.parseDouble(posInfo[2]));
        setY(Double.parseDouble(posInfo[3]));
        setZ(Double.parseDouble(posInfo[4]));
        setRoll(Double.parseDouble(posInfo[5]));
        setPitch(Double.parseDouble(posInfo[6]));
        setYaw(Double.parseDouble(posInfo[7]));
        setQuaternionX(0);
        setQuaternionY(0);
        setQuaternionZ(0);
        setQuaternionW(0);
        setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
