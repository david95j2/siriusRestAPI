package com.sierrabase.siriusapi.service.album;

import com.sierrabase.siriusapi.entity.album.AlbumPhotoEntity;
import com.sierrabase.siriusapi.entity.album.AlbumPhotoPosEntity;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoPosModel;
import com.sierrabase.siriusapi.repository.album.AlbumPhotoEntityRepository;
import com.sierrabase.siriusapi.repository.album.AlbumPhotoPosEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AlbumPhotoPosService {
    @Autowired
    private AlbumPhotoEntityRepository albumPhotoEntityRepository;
    @Autowired
    private AlbumPhotoPosEntityRepository albumPhotoPosEntityRepository;

    public ArrayList<AlbumPhotoPosModel> getAllEnitities(Integer id) {
        List<AlbumPhotoEntity> entities =  albumPhotoEntityRepository.findByAlbumId(id);

        ArrayList<AlbumPhotoPosModel> modelList = new ArrayList<AlbumPhotoPosModel>();
        for (AlbumPhotoEntity entity : entities) {
            Optional<AlbumPhotoPosEntity> albumPhotoPosEntity = albumPhotoPosEntityRepository.findByAlbumPhotoId(entity.getAlbum_photo_id());
            if(!albumPhotoPosEntity.isPresent())
                return null;
            AlbumPhotoPosModel albumPhotoPosModel = new AlbumPhotoPosModel(new AlbumPhotoModel(entity), albumPhotoPosEntity.get());
            modelList.add(albumPhotoPosModel);
        }

        return modelList;
    }

    public AlbumPhotoPosModel getEntityById(Integer id) {
        Optional<AlbumPhotoEntity> albumPhotoEntity = albumPhotoEntityRepository.findById(id);

        if(!albumPhotoEntity.isPresent())
            return null;

        AlbumPhotoModel albumPhotoModel = new AlbumPhotoModel(albumPhotoEntity.get());
        log.info("debug : "+albumPhotoModel);
        Optional<AlbumPhotoPosEntity> entity = albumPhotoPosEntityRepository.findByAlbumPhotoId(albumPhotoModel.getId());
        if (!entity.isPresent())
            return null;
        AlbumPhotoPosModel model = new AlbumPhotoPosModel(albumPhotoModel, entity.get());
        return model;
    }

    public AlbumPhotoPosModel createEntity(AlbumPhotoPosModel albumPhotoPosModel) {
        AlbumPhotoPosEntity entity = new AlbumPhotoPosEntity(albumPhotoPosModel);
        // Set properties from albumPhotoModel to entity
        entity = albumPhotoPosEntityRepository.save(entity);
        return new AlbumPhotoPosModel(albumPhotoPosModel.getPhoto(),entity);
    }

    public AlbumPhotoPosModel updateEntity(Integer id, AlbumPhotoPosModel albumPhotoPosModel) {
        Optional<AlbumPhotoPosEntity> optionalEntity = albumPhotoPosEntityRepository.findByAlbumPhotoId(id);
        if (optionalEntity.isPresent()) {
            AlbumPhotoPosEntity entity = optionalEntity.get();
            // Update properties from albumPhotoModel to entity

            entity = albumPhotoPosEntityRepository.save(new AlbumPhotoPosEntity(entity.getAlbum_photo_pos_id(), albumPhotoPosModel));
            return new AlbumPhotoPosModel(albumPhotoPosModel.getPhoto(),entity);
        } else {
            log.error("AlbumPhotoPos not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<AlbumPhotoPosEntity> optionalEntity = albumPhotoPosEntityRepository.findByAlbumPhotoId(id);
        if (optionalEntity.isPresent()) {
            albumPhotoPosEntityRepository.deleteById(optionalEntity.get().getAlbum_photo_pos_id());
            return true;
        } else {
            log.error("AlbumPhotoPos not found with id: {}", id);
            return false;
        }
    }


}