package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.dto.ResponseCodeDTO;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.dto.ResponseURLDTO;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModelingModel;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalModelService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalModelingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/facility")
public class ThreeDimensionalModelController {
    static private final String apiTag = "/api/facility";
    static private final String uri_facility = "/{facility_id}";
    static private final String uri_models = uri_facility + "/model";
    static private final String uri_model = uri_models + "/{model_id}";
    static private final String uri_export = uri_model + "/export";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    @Autowired
    private FtpConfig ftpConfig;
    private int f_id, m_id;

    private boolean parsePathVariablesOfFacility(String facilityId) {
        f_id = URIParser.parseStringToIntegerId(facilityId);
        if (f_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfModeling(String facilityId, String modelId) {
        if(!parsePathVariablesOfFacility(facilityId))
            return false;
        m_id = URIParser.parseStringToIntegerId(modelId);
        if(m_id < 0)
            return false;
        return true;
    }


    @Autowired
    private ThreeDimensionalModelService threeDimensionalModelService;

    // GET - 3D Models
    @GetMapping(uri_models)
    public ResponseEntity<?> getModels(
            @PathVariable String facility_id)
    {
        if(!parsePathVariablesOfFacility(facility_id))
            return ResponseEntity.badRequest().build();

        ArrayList<ThreeDimensionalModel> modelList = threeDimensionalModelService.getAllEntities(f_id);
//        log.info("model: " + facility);
        ResponseDTO<ArrayList<ThreeDimensionalModel>> response = ResponseDTO.<ArrayList<ThreeDimensionalModel> >builder()
                .uri(getUri(uri_models))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_model)
    public ResponseEntity<?> getModelById(
            @PathVariable String facility_id,
            @PathVariable String model_id)
    {
        if(!parsePathVariablesOfModeling(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalModel model = threeDimensionalModelService.getEntityById(m_id);
//        log.info("model: " + facility);
        ResponseDTO<ThreeDimensionalModel> response = ResponseDTO.<ThreeDimensionalModel>builder()
                .uri(getUri(uri_model))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_export)
    public ResponseEntity<?> getElevationURL(
            @PathVariable String facility_id,
            @PathVariable String model_id) {

        if(!parsePathVariablesOfModeling(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        String targetPath = threeDimensionalModelService.getElevationURL(m_id);

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
