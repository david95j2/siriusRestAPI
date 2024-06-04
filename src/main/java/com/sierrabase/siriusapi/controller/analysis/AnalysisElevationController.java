package com.sierrabase.siriusapi.controller.analysis;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityRoiModel;
import com.sierrabase.siriusapi.service.analysis.AnalysisElevationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/facilities")
public class AnalysisElevationController {
    static private final String apiTag = "/api/facilities";
    static private final String uri_facility = "/{facility_id}";
    static private final String uri_models = uri_facility + "/models";
    static private final String uri_model = uri_models + "/{model_id}";
    static private final String uri_model_rois = uri_model + "/rois";
    static private final String uri_model_roi = uri_model_rois + "/{roi_id}";
    static private final String uri_model_elevations = uri_model_roi + "/elevations";
    static private final String uri_model_elevation = uri_model_elevations + "/{elevation_id}";

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
    private AnalysisElevationService analysisElevationService;

    // POST - Elevation of the 3d model
    @PostMapping(uri_model_elevations)
    public ResponseEntity<?> createElevation(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String roi_id,
            @RequestBody ThreeDimensionalFacilityRoiModel model) {
        if(!parsePathVariablesOfModelRoi(facility_id,model_id,roi_id))
            return ResponseEntity.badRequest().build();

        analysisElevationService.createElevation(m_id,model);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_model_elevation))
                .success(true)
                .result(true)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
