package com.sierrabase.siriusapi.service.album;

import com.sierrabase.siriusapi.entity.album.AlbumPhotoEntity;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.repository.album.AlbumPhotoEntityRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AlbumPhotoService {
    @Autowired
    private AlbumPhotoEntityRepository albumPhotoEntityRepository;

    public ArrayList<AlbumPhotoModel> getAllEnitities(Integer id) {
        List<AlbumPhotoEntity> entities =  albumPhotoEntityRepository.findByAlbumId(id);

        if(entities.size() <= 0)
            return null;

        ArrayList<AlbumPhotoModel> modelList = new ArrayList<AlbumPhotoModel>();
        for (AlbumPhotoEntity entity : entities) {
            modelList.add(new AlbumPhotoModel(entity));
        }

        return modelList;
    }

    public AlbumPhotoModel getEntityById(Integer id) {
        Optional<AlbumPhotoEntity> entity = albumPhotoEntityRepository.findById(id);

        if(!entity.isPresent())
            return null;

        AlbumPhotoModel model = new AlbumPhotoModel(entity.get());

        return model;
    }

    public AlbumPhotoModel createEntity(AlbumPhotoModel albumPhotoModel) {
        AlbumPhotoEntity entity = new AlbumPhotoEntity(albumPhotoModel);
        // Set properties from albumPhotoModel to entity
        entity = albumPhotoEntityRepository.save(entity);
        return new AlbumPhotoModel(entity);
    }

    public AlbumPhotoModel updateEntity(Integer id, AlbumPhotoModel albumPhotoModel) {
        Optional<AlbumPhotoEntity> optionalEntity = albumPhotoEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            AlbumPhotoEntity entity = optionalEntity.get();
            // Update properties from albumPhotoModel to entity

            entity = albumPhotoEntityRepository.save(new AlbumPhotoEntity(entity.getAlbum_photo_id(), albumPhotoModel));
            return new AlbumPhotoModel(entity);
        } else {
            log.error("AlbumPhoto not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<AlbumPhotoEntity> optionalEntity = albumPhotoEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            albumPhotoEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("AlbumPhoto not found with id: {}", id);
            return false;
        }
    }


    public AlbumPhotoModel getEntityByName(String name) {
        Optional<AlbumPhotoEntity> entity = albumPhotoEntityRepository.findByName(name);

        if(!entity.isPresent())
            return null;

        AlbumPhotoModel model = new AlbumPhotoModel(entity.get());

        return model;
    }
}