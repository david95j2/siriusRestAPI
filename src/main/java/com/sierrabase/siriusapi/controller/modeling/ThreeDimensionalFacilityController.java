package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.dto.ResponseCodeDTO;
import com.sierrabase.siriusapi.dto.ResponseDTO;

import com.sierrabase.siriusapi.dto.ResponseURLDTO;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityRoiModel;
import com.sierrabase.siriusapi.service.album.AlbumService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/facilities")
public class ThreeDimensionalFacilityController {
    static private final String apiTag = "/api/facilities";
    static private final String uri_facility = "/{facility_id}";
    static private final String uri_models = uri_facility + "/models";
    static private final String uri_model = uri_models + "/{model_id}";
    static private final String uri_export = uri_model + "/export";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }


    private int f_id, m_id;

    private boolean parsePathVariablesOfFacility(String facilityId) {
        f_id = URIParser.parseStringToIntegerId(facilityId);
        if (f_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfModel(String facilityId, String modelId) {
        if(!parsePathVariablesOfFacility(facilityId))
            return false;
        m_id = URIParser.parseStringToIntegerId(modelId);
        if(m_id < 0)
            return false;
        return true;
    }

    @Autowired
    private ThreeDimensionalFacilityService threeDimensionalFacilityService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private FtpConfig ftpConfig;

    // GET - 3D Models
    @GetMapping(uri_models)
    public ResponseEntity<?> getThreeDimensionalFacilities(
            @PathVariable String facility_id)
    {
        if(!parsePathVariablesOfFacility(facility_id))
            return ResponseEntity.badRequest().build();

        ArrayList<ThreeDimensionalFacilityModel> modelList = threeDimensionalFacilityService.getAllEntities(f_id);
//        log.info("model: " + facility);
        ResponseDTO<ArrayList<ThreeDimensionalFacilityModel>> response = ResponseDTO.<ArrayList<ThreeDimensionalFacilityModel> >builder()
                .uri(getUri(uri_models))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a 3d model
    @GetMapping(uri_model)
    public ResponseEntity<?> getThreeDimensionalFacilityById(
            @PathVariable String facility_id,
            @PathVariable String model_id)
    {
        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityModel model = threeDimensionalFacilityService.getEntityById(m_id);
//        log.info("model: " + facility);
        ResponseDTO<ThreeDimensionalFacilityModel> response = ResponseDTO.<ThreeDimensionalFacilityModel>builder()
                .uri(getUri(uri_model))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(uri_model)
    public ResponseEntity<?> updateThreeDimensionalFacility(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @RequestBody ThreeDimensionalFacilityModel model) {

        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityModel updatedModel = threeDimensionalFacilityService.updateEntity(m_id, model);
        log.info("model: " + updatedModel);
        ResponseDTO<ThreeDimensionalFacilityModel> response = ResponseDTO.<ThreeDimensionalFacilityModel>builder()
                .uri(getUri(uri_model))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(uri_model)
    public ResponseEntity<?> deleteThreeDimensionalFacilityROI(
            @PathVariable String facility_id,
            @PathVariable String model_id) {

        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = threeDimensionalFacilityService.deleteEntity(m_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_model))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // webhookListener : From Linux
    @PostMapping(uri_models)
    public ResponseEntity<?> webhookListener(
            @PathVariable String facility_id,
            @RequestBody SourceInfoModel model) {

        if (!parsePathVariablesOfFacility(facility_id)) {
            ResponseEntity.badRequest().build();
        }

        AlbumModel albumModel = albumService.getEntityById(model.getAlbumId());
        ThreeDimensionalFacilityModel threeDimensionalFacilityModel = new ThreeDimensionalFacilityModel(f_id, albumModel);
        threeDimensionalFacilityModel.setThreeDimensionalFacilityUrl(URICreator.pathToString(ftpConfig.getNginxUri(),"model"));
        ThreeDimensionalFacilityModel createdTdf = threeDimensionalFacilityService.createEntity(threeDimensionalFacilityModel);

        boolean importResult = threeDimensionalFacilityService.importModel(createdTdf.getId(), model);

        if (!importResult) {
            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Can not import 3D model"));
        }


        createdTdf.setThreeDimensionalFacilityUrl(URICreator.pathToString(ftpConfig.getNginxUri(),"model",String.valueOf(createdTdf.getId()),"downsampled_model.glb"));
        threeDimensionalFacilityService.updateEntity(createdTdf.getId(),createdTdf);

        log.info("create 3d facility : "+createdTdf);

        boolean createResult = threeDimensionalFacilityService.createGLTF(createdTdf);

        if (!createResult) {
            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Can not create GLTF model"));
        }


        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_models))
                .success(createResult)
                .result(createResult)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @GetMapping(uri_export)
    public ResponseEntity<?> getElevationURL(
            @PathVariable String facility_id,
            @PathVariable String model_id) {

        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        String targetPath = threeDimensionalFacilityService.getElevationURL(m_id);

        if (targetPath== null) {
            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false,HttpStatus.NOT_FOUND.value(),"Elevation creation task did not proceed."));
        }
//        log.info("model: " + facility);


        ResponseDTO<ResponseURLDTO> response = ResponseDTO.<ResponseURLDTO>builder()
                .uri(getUri(uri_export))
                .success(true)
                .result(new ResponseURLDTO<>(targetPath))
                .build();

        return ResponseEntity.ok().body(response);
    }
}
