package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.modeling.InspectionNetworkMapModel;
import com.sierrabase.siriusapi.service.modeling.InspectionNetworkMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/facilities")
public class InspectionNetworkMapController {
    static private final String apiTag = "/api/facilities";
    static private final String uri_facility = "/{facility_id}";
    static private final String uri_models = uri_facility + "/models";
    static private final String uri_model = uri_models + "/{model_id}";
    static private final String uri_inspection_network_maps = uri_model + "/inspection-network-maps";
    static private final String uri_inspection_network_map = uri_inspection_network_maps + "/{inspection_network_map_id}";


    private String getUri(final String methodName) {
        return apiTag + methodName;
    }


    private int f_id, m_id, n_c_id;

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

    private boolean parsePathVariablesOfInspectionNetworkMap(String facilityId, String modelId, String inspectionNetworkMaps) {
        if(!parsePathVariablesOfModel(facilityId, modelId))
            return false;

        n_c_id = URIParser.parseStringToIntegerId(inspectionNetworkMaps);

        if(n_c_id < 0)
            return false;

        return true;
    }


    @Autowired
    private InspectionNetworkMapService inspectionNetworkMapService;

    // GET - a facility
    @GetMapping(uri_inspection_network_maps)
    public ResponseEntity<?> getNetworkOfCracks(
            @PathVariable String facility_id,
            @PathVariable String model_id)
    {

        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        ArrayList<InspectionNetworkMapModel> modelList = inspectionNetworkMapService.getAllEntities(m_id);
//        log.info("model: " + facility);
        ResponseDTO<ArrayList<InspectionNetworkMapModel>> response = ResponseDTO.<ArrayList<InspectionNetworkMapModel> >builder()
                .uri(getUri(uri_inspection_network_maps))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_inspection_network_map)
    public ResponseEntity<?> getNetworkOfCrack(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String inspection_network_map_id)
    {
        if(!parsePathVariablesOfInspectionNetworkMap(facility_id,model_id,inspection_network_map_id))
            return ResponseEntity.badRequest().build();

        InspectionNetworkMapModel model = inspectionNetworkMapService.getEntityById(n_c_id);
//        log.info("model: " + facility);
        ResponseDTO<InspectionNetworkMapModel> response = ResponseDTO.<InspectionNetworkMapModel>builder()
                .uri(getUri(uri_inspection_network_map))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @PostMapping(uri_inspection_network_maps)
    public ResponseEntity<?> createNetworkOfCrack(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @RequestBody InspectionNetworkMapModel model) {
        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        InspectionNetworkMapModel createdModel = inspectionNetworkMapService.createEntity(model);
        log.info("model: " + createdModel);
        ResponseDTO<InspectionNetworkMapModel> response = ResponseDTO.<InspectionNetworkMapModel>builder()
                .uri(getUri(uri_inspection_network_maps))
                .success(createdModel != null)
                .result(createdModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(uri_inspection_network_map)
    public ResponseEntity<?> updateNetworkOfCrack(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String inspection_network_map_id,
            @RequestBody InspectionNetworkMapModel model) {

        if(!parsePathVariablesOfInspectionNetworkMap(facility_id,model_id,inspection_network_map_id))
            return ResponseEntity.badRequest().build();

        InspectionNetworkMapModel updatedModel = inspectionNetworkMapService.updateEntity(n_c_id, model);
        log.info("model: " + updatedModel);
        ResponseDTO<InspectionNetworkMapModel> response = ResponseDTO.<InspectionNetworkMapModel>builder()
                .uri(getUri(uri_inspection_network_map))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(uri_inspection_network_map)
    public ResponseEntity<?> deleteNetworkOfCrack(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String inspection_network_map_id) {

        if(!parsePathVariablesOfInspectionNetworkMap(facility_id,model_id,inspection_network_map_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = inspectionNetworkMapService.deleteEntity(n_c_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_inspection_network_map))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
