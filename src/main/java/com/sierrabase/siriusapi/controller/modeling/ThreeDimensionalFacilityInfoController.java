package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityInfoModel;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityInfoService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class ThreeDimensionalFacilityInfoController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_modelings = uri_album + "/modeling";
    static private final String uri_modeling = uri_modelings + "/{modeling_id}";

    static private final String uri_models = uri_modeling + "/model";
    static private final String uri_model = uri_models + "/{model_id}";
    static private final String uri_model_infos = uri_model + "/infos";
    static private final String uri_model_info = uri_model_infos + "/{info_id}";


    private String getUri(final String methodName) {
        return apiTag + methodName;
    }


    private int a_id, m_id, m_m_id, m_m_i_id;

    private boolean parsePathVariablesOfAlbum(String albumId) {
        a_id = URIParser.parseStringToIntegerId(albumId);
        if (a_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfModeling(String albumId, String modelingId) {
        if(!parsePathVariablesOfAlbum(albumId))
            return false;
        m_id = URIParser.parseStringToIntegerId(modelingId);
        if(m_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfModel(String albumId, String modelingId, String modelId) {
        if(!parsePathVariablesOfModeling(albumId, modelingId))
            return false;

        m_m_id = URIParser.parseStringToIntegerId(modelId);

        if(m_m_id < 0)
            return false;

        return true;
    }

    private boolean parsePathVariablesOfInfo(String albumId, String modelingId, String modelId, String infoId) {
        if(!parsePathVariablesOfModel(albumId, modelingId,modelId))
            return false;

        m_m_i_id = URIParser.parseStringToIntegerId(infoId);

        if(m_m_i_id < 0)
            return false;

        return true;
    }


    @Autowired
    private ThreeDimensionalFacilityInfoService threeDimensionalFacilityInfoService;

    // GET - a facility
    @GetMapping(uri_model_infos)
    public ResponseEntity<?> getThreeDimensionalFacilityInfos(
            @PathVariable String album_id,
            @PathVariable String modeling_id,
            @PathVariable String model_id)
    {

        if(!parsePathVariablesOfModel(album_id, modeling_id,model_id))
            return ResponseEntity.badRequest().build();

        ArrayList<ThreeDimensionalFacilityInfoModel> modelList = threeDimensionalFacilityInfoService.getAllEntities(m_m_id);
//        log.info("model: " + facility);
        ResponseDTO<ArrayList<ThreeDimensionalFacilityInfoModel>> response = ResponseDTO.<ArrayList<ThreeDimensionalFacilityInfoModel> >builder()
                .uri(getUri(uri_model_infos))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_model_info)
    public ResponseEntity<?> getThreeDimensionalFacilityInfoById(
            @PathVariable String album_id,
            @PathVariable String modeling_id,
            @PathVariable String model_id,
            @PathVariable String info_id)
    {
        if(!parsePathVariablesOfInfo(album_id,modeling_id,model_id,info_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityInfoModel model = threeDimensionalFacilityInfoService.getEntityById(m_m_i_id);
//        log.info("model: " + facility);
        ResponseDTO<ThreeDimensionalFacilityInfoModel> response = ResponseDTO.<ThreeDimensionalFacilityInfoModel>builder()
                .uri(getUri(uri_model_info))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @PostMapping(uri_model_infos)
    public ResponseEntity<?> createThreeDimensionalFacilityInfo(
            @PathVariable String album_id,
            @PathVariable String modeling_id,
            @PathVariable String model_id,
            @RequestBody ThreeDimensionalFacilityInfoModel model) {
        if(!parsePathVariablesOfModel(album_id,modeling_id,model_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityInfoModel createdModel = threeDimensionalFacilityInfoService.createEntity(model);
        log.info("model: " + createdModel);
        ResponseDTO<ThreeDimensionalFacilityInfoModel> response = ResponseDTO.<ThreeDimensionalFacilityInfoModel>builder()
                .uri(getUri(uri_model_infos))
                .success(createdModel != null)
                .result(createdModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(uri_model_info)
    public ResponseEntity<?> updateThreeDimensionalFacilityInfo(
            @PathVariable String album_id,
            @PathVariable String modeling_id,
            @PathVariable String model_id,
            @PathVariable String info_id,
            @RequestBody ThreeDimensionalFacilityInfoModel model) {

        if(!parsePathVariablesOfInfo(album_id,modeling_id,model_id,info_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityInfoModel updatedModel = threeDimensionalFacilityInfoService.updateEntity(m_m_i_id, model);
        log.info("model: " + updatedModel);
        ResponseDTO<ThreeDimensionalFacilityInfoModel> response = ResponseDTO.<ThreeDimensionalFacilityInfoModel>builder()
                .uri(getUri(uri_model_info))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(uri_model_info)
    public ResponseEntity<?> deleteThreeDimensionalFacilityInfo(
            @PathVariable String album_id,
            @PathVariable String modeling_id,
            @PathVariable String model_id,
            @PathVariable String info_id) {

        if(!parsePathVariablesOfInfo(album_id,modeling_id,model_id,info_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = threeDimensionalFacilityInfoService.deleteEntity(m_m_i_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_model_info))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
