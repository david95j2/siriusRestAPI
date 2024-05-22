package com.sierrabase.siriusapi.controller.album;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoPosModel;
import com.sierrabase.siriusapi.service.album.AlbumPhotoPosService;
import com.sierrabase.siriusapi.service.album.AlbumPhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class AlbumPhotoController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_album_photos = uri_album + "/photos";
    static private final String uri_album_photo = uri_album_photos + "/{album_photo_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int a_id, a_p_id;
    private boolean parsePathVariablesOfAlbum(String albumId) {
        a_id = URIParser.parseStringToIntegerId(albumId);
        if(a_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfAlbumPhoto(String albumId, String albumPhotoId) {
        if(!parsePathVariablesOfAlbum(albumId))
            return false;
        a_p_id = URIParser.parseStringToIntegerId(albumPhotoId);
        if(a_p_id < 0)
            return false;
        return true;
    }
    @Autowired
    private AlbumPhotoService albumPhotoService;

    @Autowired
    private AlbumPhotoPosService albumPhotoPosService;

    // GET - albumPhotos
    @GetMapping(uri_album_photos)
    public ResponseEntity<?> getAlbumPhotos(@PathVariable String album_id) {

        if(!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }

        ArrayList<AlbumPhotoPosModel> modelList = albumPhotoPosService.getAllEnitities(a_id);
//        log.info("model: " + modelList);
        ResponseDTO<ArrayList<AlbumPhotoPosModel>> response = ResponseDTO.<ArrayList<AlbumPhotoPosModel>>builder()
                .uri(getUri(uri_album_photos))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a albumPhoto
    @GetMapping(uri_album_photo)
    public ResponseEntity<?> getAlbumPhoto(@PathVariable String album_id,
                                           @PathVariable String album_photo_id) {

        if(!parsePathVariablesOfAlbumPhoto(album_id,album_photo_id)) {
            ResponseEntity.badRequest().build();
        }

        AlbumPhotoPosModel model = albumPhotoPosService.getEntityById(a_p_id);

//        log.info("model: " + model);
        ResponseDTO<AlbumPhotoPosModel> response = ResponseDTO.<AlbumPhotoPosModel>builder()
                .uri(getUri(uri_album_photo))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a albumPhoto
    @PostMapping(uri_album_photos)
    public ResponseEntity<?> createAlbumPhoto(
            @PathVariable String album_id,
            @RequestBody AlbumPhotoPosModel model) {

        if(!parsePathVariablesOfAlbum(album_id))
            return ResponseEntity.badRequest().build();

        AlbumPhotoModel albumPhotoModel = albumPhotoService.createEntity(model.getPhoto());
        if (albumPhotoModel == null)
            return ResponseEntity.badRequest().build();
        model.setPhoto(albumPhotoModel);

        AlbumPhotoPosModel createdModel = albumPhotoPosService.createEntity(model);

//        log.info("model: " + createdModel);
        ResponseDTO<AlbumPhotoPosModel> response = ResponseDTO.<AlbumPhotoPosModel>builder()
                .uri(getUri(uri_album_photos))
                .success(createdModel != null)
                .result(createdModel)
                .build();
        return ResponseEntity.ok().body(response);
    }

    // PUT - a albumPhoto
    @PutMapping(uri_album_photo)
    public ResponseEntity<?> updateAlbumPhoto(
            @PathVariable String album_id,
            @PathVariable String album_photo_id,
            @RequestBody AlbumPhotoPosModel model)
    {
        if(!parsePathVariablesOfAlbumPhoto(album_id,album_photo_id))
            return ResponseEntity.badRequest().build();

        AlbumPhotoModel updatedAPModel = albumPhotoService.updateEntity(a_p_id, model.getPhoto());
        model.setPhoto(updatedAPModel);
        AlbumPhotoPosModel updatedModel = albumPhotoPosService.updateEntity(a_p_id, model);

//        log.info("model: " + updatedModel);
        ResponseDTO<AlbumPhotoPosModel> response = ResponseDTO.<AlbumPhotoPosModel>builder()
                .uri(getUri(uri_album_photo))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a albumPhoto
    @DeleteMapping(uri_album_photo)
    public ResponseEntity<?> deleteAlbumPhoto(
            @PathVariable String album_id,
            @PathVariable String album_photo_id)
    {
        if(!parsePathVariablesOfAlbumPhoto(album_id, album_photo_id))
            return ResponseEntity.badRequest().build();

        boolean deletedAlbumPhoto = albumPhotoPosService.deleteEntity(a_p_id);
        boolean deleted = albumPhotoService.deleteEntity(a_p_id);
//        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_album_photo))
                .success(deletedAlbumPhoto && deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

}