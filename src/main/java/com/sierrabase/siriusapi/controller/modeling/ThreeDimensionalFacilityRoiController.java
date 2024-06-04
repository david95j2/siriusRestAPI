package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityRoiModel;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityRoiService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/facilities")
public class ThreeDimensionalFacilityRoiController {
    static private final String apiTag = "/api/facilities";
    static private final String uri_facility = "/{facility_id}";
    static private final String uri_models = uri_facility + "/models";
    static private final String uri_model = uri_models + "/{model_id}";
    static private final String uri_model_rois = uri_model + "/rois";
    static private final String uri_model_roi = uri_model_rois + "/{roi_id}";


    private String getUri(final String methodName) {
        return apiTag + methodName;
    }


    private int f_id, m_id, m_r_id;

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

    private boolean parsePathVariablesOfModelRoi(String facilityId, String modelId, String modelRoiId) {
        if(!parsePathVariablesOfModel(facilityId, modelId))
            return false;

        m_r_id = URIParser.parseStringToIntegerId(modelRoiId);

        if(m_r_id < 0)
            return false;

        return true;
    }


    @Autowired
    private ThreeDimensionalFacilityRoiService threeDimensionalFacilityRoiService;

    // GET - model ROIS
    @GetMapping(uri_model_rois)
    public ResponseEntity<?> getThreeDimensionalFacilityROIs(
            @PathVariable String facility_id,
            @PathVariable String model_id)
    {

        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        ArrayList<ThreeDimensionalFacilityRoiModel> modelList = threeDimensionalFacilityRoiService.getAllEntities(m_id);
//        log.info("model: " + facility);


        ResponseDTO<ArrayList<ThreeDimensionalFacilityRoiModel>> response = ResponseDTO.<ArrayList<ThreeDimensionalFacilityRoiModel> >builder()
                .uri(getUri(uri_model_rois))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_model_roi)
    public ResponseEntity<?> getThreeDimensionalFacilityROIById(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String roi_id)
    {
        if(!parsePathVariablesOfModelRoi(facility_id,model_id,roi_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityRoiModel model = threeDimensionalFacilityRoiService.getEntityById(m_r_id);
//        log.info("model: " + facility);
        ResponseDTO<ThreeDimensionalFacilityRoiModel> response = ResponseDTO.<ThreeDimensionalFacilityRoiModel>builder()
                .uri(getUri(uri_model_roi))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @PostMapping(uri_model_rois)
    public ResponseEntity<?> createThreeDimensionalFacilityROI(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @RequestBody ThreeDimensionalFacilityRoiModel model) {
        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityRoiModel createdModel = threeDimensionalFacilityRoiService.createEntity(model);
        log.info("model: " + createdModel);
        ResponseDTO<ThreeDimensionalFacilityRoiModel> response = ResponseDTO.<ThreeDimensionalFacilityRoiModel>builder()
                .uri(getUri(uri_model_rois))
                .success(createdModel != null)
                .result(createdModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(uri_model_roi)
    public ResponseEntity<?> updateThreeDimensionalFacilityROI(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String roi_id,
            @RequestBody ThreeDimensionalFacilityRoiModel model) {

        if(!parsePathVariablesOfModelRoi(facility_id,model_id,roi_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalFacilityRoiModel updatedModel = threeDimensionalFacilityRoiService.updateEntity(m_r_id, model);
        log.info("model: " + updatedModel);
        ResponseDTO<ThreeDimensionalFacilityRoiModel> response = ResponseDTO.<ThreeDimensionalFacilityRoiModel>builder()
                .uri(getUri(uri_model_roi))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(uri_model_roi)
    public ResponseEntity<?> deleteThreeDimensionalFacilityROI(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String roi_id) {

        if(!parsePathVariablesOfModelRoi(facility_id,model_id,roi_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = threeDimensionalFacilityRoiService.deleteEntity(m_r_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_model_roi))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
