package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.FacilityModel;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModelingModel;
import com.sierrabase.siriusapi.service.album.ModelingService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalModelingService;
import com.sierrabase.siriusapi.service.worker.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class ThreeDimensionalModelingController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_modelings = uri_album + "/modeling";
    static private final String uri_modeling = uri_modelings + "/{modeling_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }


    private int a_id, m_id;

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



    @Autowired
    private WorkerService workerService;
    @Autowired
    private ModelingService modelingService;
    @Autowired
    private ThreeDimensionalModelingService threeDimensionalModelingService;

    // GET - a facility
    @GetMapping(uri_modelings)
    public ResponseEntity<?> getModelings(
            @PathVariable String album_id)
    {
        if(!parsePathVariablesOfAlbum(album_id))
            return ResponseEntity.badRequest().build();

        ArrayList<ThreeDimensionalModelingModel> modelList = threeDimensionalModelingService.getAllEntities(a_id);
//        log.info("model: " + facility);
        ResponseDTO<ArrayList<ThreeDimensionalModelingModel>> response = ResponseDTO.<ArrayList<ThreeDimensionalModelingModel> >builder()
                .uri(getUri(uri_modeling))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_modeling)
    public ResponseEntity<?> getModelingById(
            @PathVariable String album_id,
            @PathVariable String modeling_id)
    {
        if(!parsePathVariablesOfModeling(album_id,modeling_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalModelingModel model = threeDimensionalModelingService.getEntityById(m_id);
//        log.info("model: " + facility);
        ResponseDTO<ThreeDimensionalModelingModel> response = ResponseDTO.<ThreeDimensionalModelingModel>builder()
                .uri(getUri(uri_modeling))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(uri_modeling)
    public ResponseEntity<?> webhookListener(
            @PathVariable String album_id,
            @RequestBody SourceInfoModel model) {

        if (!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }

        boolean result = modelingService.importModel(a_id,model);



        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_modeling))
//                .success(modelList != null)
//                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
