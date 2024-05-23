package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;

import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class ThreeDimensionalFacilityController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_modelings = uri_album + "/modeling";
    static private final String uri_modeling = uri_modelings + "/{modeling_id}";

    static private final String uri_models = uri_modeling + "/model";
    static private final String uri_model = uri_models + "/{model_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }


    private int a_id, m_id, m_m_id;

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


    @Autowired
    private ThreeDimensionalFacilityService threeDimensionalFacilityService;

    // GET - a facility
    @GetMapping(uri_models)
    public ResponseEntity<?> getThreeDimensionalFacilities(
            @PathVariable String album_id,
            @PathVariable String modeling_id)
    {
        if(!parsePathVariablesOfModeling(album_id, modeling_id))
            return ResponseEntity.badRequest().build();

        ArrayList<ThreeDimensionalFacilityModel> modelList = threeDimensionalFacilityService.getAllEntities(a_id);
//        log.info("model: " + facility);
        ResponseDTO<ArrayList<ThreeDimensionalFacilityModel>> response = ResponseDTO.<ArrayList<ThreeDimensionalFacilityModel> >builder()
                .uri(getUri(uri_models))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_model)
    public ResponseEntity<?> getThreeDimensionalFacilityById(
            @PathVariable String album_id,
            @PathVariable String modeling_id,
            @PathVariable String model_id)
    {
        if(!parsePathVariablesOfModel(album_id,modeling_id,model_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityModel model = threeDimensionalFacilityService.getEntityById(m_m_id);
//        log.info("model: " + facility);
        ResponseDTO<ThreeDimensionalFacilityModel> response = ResponseDTO.<ThreeDimensionalFacilityModel>builder()
                .uri(getUri(uri_model))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
