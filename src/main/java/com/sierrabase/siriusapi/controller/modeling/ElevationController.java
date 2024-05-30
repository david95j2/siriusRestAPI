package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseCodeDTO;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityInfoModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModel;
import com.sierrabase.siriusapi.service.analysis.AnalysisService;
import com.sierrabase.siriusapi.service.modeling.ElevationService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalModelService;
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
@RequestMapping("/api/albums")
public class ElevationController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_modelings = uri_album + "/modeling";
    static private final String uri_modeling = uri_modelings + "/{modeling_id}";

    static private final String uri_models = uri_modeling + "/model";
    static private final String uri_model = uri_models + "/{model_id}";

    static private final String uri_model_elevation = uri_model + "/elevation";

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
    private ElevationService elevationService;
    @Autowired
    private AnalysisService analysisService;
    @Autowired
    private ThreeDimensionalModelService threeDimensionalModelService;
    // POST - Elevation of the 3d model
    @PostMapping(uri_model_elevation)
    public ResponseEntity<?> createElevation(
            @PathVariable String album_id,
            @PathVariable String modeling_id,
            @PathVariable String model_id,
            @RequestBody ThreeDimensionalFacilityInfoModel model) {
        if(!parsePathVariablesOfModel(album_id,modeling_id,model_id))
            return ResponseEntity.badRequest().build();

        elevationService.createElevation(a_id,m_m_id,model);
//        ThreeDimensionalModel threeDimensionalModel = threeDimensionalModelService.getEntityById(m_m_id);
//        threeDimensionalModel.setElevationStatus("Running");
//        threeDimensionalModelService.updateEntity(threeDimensionalModel.getId(), threeDimensionalModel);
//
//        Boolean result = elevationService.createElevationFiles(m_m_id,model);
//        if (!result) {
//            log.error("Error create elevation");
//            threeDimensionalModel.setElevationStatus("Error");
//            threeDimensionalModelService.updateEntity(threeDimensionalModel.getId(), threeDimensionalModel);
//            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Can not create model"));
//        }
//
//        AnalysisModel analysisModel = new AnalysisModel();
//        analysisModel.setAlbumId(a_id);
//        analysisModel.setName("elevation crack");
//        analysisModel.setType(1);
//        analysisModel.setTypeName("Segmentation");
//        analysisModel.setStatus("Running");
//        analysisModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
//        AnalysisModel createdAnalysisModel = analysisService.createEntity(analysisModel);
//
//        log.info("Elevation analysis :"+createdAnalysisModel);
//        elevationService.createAnalysisCrack(m_m_id,model.getId(),createdAnalysisModel, threeDimensionalModel);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_model_elevation))
                .success(true)
                .result(true)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
