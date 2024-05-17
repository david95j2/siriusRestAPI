package com.sierrabase.siriusapi.entity.album;

import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoPosModel;
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
@Table(name="album_photos_pos")
public class AlbumPhotoPosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_photo_pos_id", unique = true, nullable = false)
    private int album_photo_pos_id;
    @Column(name = "album_photo_id")
    private int album_photo_id;
    @Column(name = "x")
    private double x;
    @Column(name = "y")
    private double y;
    @Column(name = "z")
    private double z;
    @Column(name = "roll")
    private double roll;
    @Column(name = "pitch")
    private double pitch;
    @Column(name = "yaw")
    private double yaw;
    @Column(name = "quaternion_x")
    private double quaternionX;
    @Column(name = "quaternion_y")
    private double quaternionY;
    @Column(name = "quaternion_z")
    private double quaternionZ;
    @Column(name = "quaternion_w")
    private double quaternionW;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public AlbumPhotoPosEntity(final AlbumPhotoPosModel model) {
        setAlbum_photo_pos_id(model.getId());
        setAlbum_photo_id(model.getPhoto().getId());
        setX(model.getX());
        setY(model.getY());
        setZ(model.getZ());
        setRoll(model.getRoll());
        setPitch(model.getPitch());
        setYaw(model.getYaw());
        setQuaternionX(model.getQuaternionX());
        setQuaternionY(model.getQuaternionY());
        setQuaternionZ(model.getQuaternionZ());
        setQuaternionW(model.getQuaternionW());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }




    public AlbumPhotoPosEntity(final Integer id, AlbumPhotoPosModel model) {
        this(model);
        setAlbum_photo_pos_id(id);
    }
}
