package com.sierrabase.siriusapi.controller.album;




import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseCodeDTO;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.album.AlbumModel;

import com.sierrabase.siriusapi.service.album.AlbumResource;
import com.sierrabase.siriusapi.service.album.AlbumService;

import com.sierrabase.siriusapi.service.worker.WorkerService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";

    static private final String uri_import = uri_album + "/import";
    static private final String uri_export = uri_album + "/export";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int a_id;

    private boolean parsePathVariablesOfAlbum(String albumId) {
        a_id = URIParser.parseStringToIntegerId(albumId);
        if (a_id < 0)
            return false;
        return true;
    }


    @Autowired
    private AlbumService albumService;
    @Autowired
    private WorkerService workerService;

    @Value("${path.repository.base}")
    private String repository_path;

    // GET - albums
    @GetMapping()
    public ResponseEntity<?> getAlbums(@RequestParam Optional<Integer> facility_id,
                                       @RequestParam Optional<Integer> facility_map_id) {

        ArrayList<AlbumModel> modelList = albumService.getByConditions(facility_id, facility_map_id);
        log.info("model: " + modelList);
        ResponseDTO<ArrayList<AlbumModel>> response = ResponseDTO.<ArrayList<AlbumModel>>builder()
                .uri(apiTag)
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - album
    @GetMapping(uri_album)
    public ResponseEntity<?> getAlbum(@PathVariable String album_id) {

        if (!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }

        AlbumModel model = albumService.getEntityById(a_id);
        log.info("model: " + model);
        ResponseDTO<AlbumModel> response = ResponseDTO.<AlbumModel>builder()
                .uri(getUri(uri_album))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - album
    @PostMapping()
    public ResponseEntity<?> postAlbum(@RequestBody AlbumModel model) {
        AlbumModel createdModel = albumService.createEntity(model);
        log.info("model: " + createdModel);
        ResponseDTO<AlbumModel> response = ResponseDTO.<AlbumModel>builder()
                .uri(getUri(uri_album))
                .success(createdModel != null)
                .result(createdModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a album
    @PutMapping(uri_album)
    public ResponseEntity<?> updateAlbum(
            @PathVariable(required = true) String album_id,
            @RequestBody AlbumModel model) {
        if (!parsePathVariablesOfAlbum(album_id))
            return ResponseEntity.badRequest().build();

        AlbumModel updatedModel = albumService.updateEntity(a_id, model);
//        log.info("model: " + updatedModel);
        ResponseDTO<AlbumModel> response = ResponseDTO.<AlbumModel>builder()
                .uri(getUri(uri_album))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a album
    @DeleteMapping(uri_album)
    public ResponseEntity<?> deleteAlbum(
            @PathVariable(required = true) String album_id) {
        if (!parsePathVariablesOfAlbum(album_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = albumService.deleteEntity(a_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_album))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @PostMapping(uri_import)
    public ResponseEntity<?> importAlbum(@PathVariable String album_id,
                                         @RequestBody SourceInfoModel model) {

        if (!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }
//        String localPath = Paths.get(repository_path, "album", String.valueOf(a_id)).toString();

        boolean result = albumService.importAlbum(a_id, model);
//        albumWorker = new AlbumWorker(album_id);
//        albumWorker.loadAlbumImagesTo(albumRepository);
//
//        albumService.loadAlbumImagesTo
//        boolean result = workerService.ftpDownload(a_id, localPath, model, true);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_import))
                .success(result)
                .result(result)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_export)
    public ResponseEntity<?> exportAlbum(@PathVariable String album_id) {

        if (!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }

        String targetPath = albumService.exportAlbum(a_id);
//        String localPath = Paths.get(repository_path, "album", String.valueOf(a_id)).toString();
//        String zipFilePath = workerService.download(a_id,localPath);
//        AlbumResource zipModel = new AlbumResource();
//        zipModel.setZipFilePath(zipFilePath);
        if (targetPath == null) {
            ResponseEntity.internalServerError().build();
        }

        ResponseDTO<ResponseCodeDTO> response = ResponseDTO.<ResponseCodeDTO>builder()
                .uri(getUri(uri_export))
                .success(true)
                .result(new ResponseCodeDTO<>(true, HttpStatus.OK.value(),targetPath))
                .build();
        return ResponseEntity.ok().body(response);
    }


}
