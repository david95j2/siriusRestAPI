package com.sierrabase.siriusapi.controller.album;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.album.CameraModel;
import com.sierrabase.siriusapi.service.album.CameraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/cameras")
public class CameraController {
    static private final String apiTag = "/api/cameras";
    static private final String uri_camera = "/{camera_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int c_id;
    private boolean parsePathVariablesOfCamera(String cameraId) {
        c_id = URIParser.parseStringToIntegerId(cameraId);
        if(c_id < 0)
            return false;
        return true;
    }

    @Autowired
    private CameraService cameraService;

    // GET - cameras
    @GetMapping()
    public ResponseEntity<?> getCameras() {
        ArrayList<CameraModel> modelList = cameraService.getAllEntities();
        log.info("model: " + modelList);
        ResponseDTO<ArrayList<CameraModel>> response = ResponseDTO.<ArrayList<CameraModel>>builder()
                .uri(apiTag)
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a camera
    @GetMapping(uri_camera)
    public ResponseEntity<?> getCamera(
            @PathVariable(required=true)String camera_id) {

        if(!parsePathVariablesOfCamera(camera_id))
            return ResponseEntity.badRequest().build();

        CameraModel model = cameraService.getEntityById(c_id);
        log.info("model: " + model);
        ResponseDTO<CameraModel> response = ResponseDTO.<CameraModel>builder()
                .uri(getUri(uri_camera))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a camera
    @PostMapping()
    public ResponseEntity<?> createCamera(@RequestBody CameraModel model) {
        CameraModel createdModel = cameraService.createEntity(model);
        log.info("model: " + createdModel);
        ResponseDTO<CameraModel> response = ResponseDTO.<CameraModel>builder()
                .uri(apiTag)
                .success(createdModel != null)
                .result(createdModel)
                .build();
        return ResponseEntity.ok().body(response);
    }

    // PUT - a camera
    @PutMapping(uri_camera)
    public ResponseEntity<?> updateCamera(
            @PathVariable(required=true)String camera_id,
            @RequestBody CameraModel model)
    {
        if(!parsePathVariablesOfCamera(camera_id))
            return ResponseEntity.badRequest().build();

        CameraModel updatedModel = cameraService.updateEntity(c_id, model);
        log.info("model: " + updatedModel);
        ResponseDTO<CameraModel> response = ResponseDTO.<CameraModel>builder()
                .uri(getUri(uri_camera))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a camera
    @DeleteMapping(uri_camera)
    public ResponseEntity<?> deleteCamera(
            @PathVariable(required=true)String camera_id)
    {
        if(!parsePathVariablesOfCamera(camera_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = cameraService.deleteEntity(c_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_camera))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
