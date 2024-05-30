package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.modeling.NetworkOfCrackModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityInfoModel;
import com.sierrabase.siriusapi.service.modeling.NetworkOfCrackService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/facility")
public class NetworkOfCrackController {
    static private final String apiTag = "/api/facility";
    static private final String uri_facility = "/{facility_id}";
    static private final String uri_models = uri_facility + "/model";
    static private final String uri_model = uri_models + "/{model_id}";
    static private final String uri_crack_networks = uri_model + "/crack-network";
    static private final String uri_crack_network = uri_crack_networks + "/{crack_network_id}";


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

    private boolean parsePathVariablesOfNetworkOfCrack(String facilityId, String modelId, String networkCrackId) {
        if(!parsePathVariablesOfModel(facilityId, modelId))
            return false;

        n_c_id = URIParser.parseStringToIntegerId(networkCrackId);

        if(n_c_id < 0)
            return false;

        return true;
    }


    @Autowired
    private NetworkOfCrackService networkOfCrackService;

    // GET - a facility
    @GetMapping(uri_crack_networks)
    public ResponseEntity<?> getNetworkOfCracks(
            @PathVariable String facility_id,
            @PathVariable String model_id)
    {

        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        ArrayList<NetworkOfCrackModel> modelList = networkOfCrackService.getAllEntities(m_id);
//        log.info("model: " + facility);
        ResponseDTO<ArrayList<NetworkOfCrackModel>> response = ResponseDTO.<ArrayList<NetworkOfCrackModel> >builder()
                .uri(getUri(uri_crack_networks))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_crack_network)
    public ResponseEntity<?> getNetworkOfCrack(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String crack_network_id)
    {
        if(!parsePathVariablesOfNetworkOfCrack(facility_id,model_id,crack_network_id))
            return ResponseEntity.badRequest().build();

        NetworkOfCrackModel model = networkOfCrackService.getEntityById(n_c_id);
//        log.info("model: " + facility);
        ResponseDTO<NetworkOfCrackModel> response = ResponseDTO.<NetworkOfCrackModel>builder()
                .uri(getUri(uri_crack_network))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @PostMapping(uri_crack_networks)
    public ResponseEntity<?> createNetworkOfCrack(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @RequestBody NetworkOfCrackModel model) {
        if(!parsePathVariablesOfModel(facility_id,model_id))
            return ResponseEntity.badRequest().build();

        NetworkOfCrackModel createdModel = networkOfCrackService.createEntity(model);
        log.info("model: " + createdModel);
        ResponseDTO<NetworkOfCrackModel> response = ResponseDTO.<NetworkOfCrackModel>builder()
                .uri(getUri(uri_crack_networks))
                .success(createdModel != null)
                .result(createdModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping(uri_crack_network)
    public ResponseEntity<?> updateNetworkOfCrack(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String crack_network_id,
            @RequestBody NetworkOfCrackModel model) {

        if(!parsePathVariablesOfNetworkOfCrack(facility_id,model_id,crack_network_id))
            return ResponseEntity.badRequest().build();

        NetworkOfCrackModel updatedModel = networkOfCrackService.updateEntity(n_c_id, model);
        log.info("model: " + updatedModel);
        ResponseDTO<NetworkOfCrackModel> response = ResponseDTO.<NetworkOfCrackModel>builder()
                .uri(getUri(uri_crack_network))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(uri_crack_network)
    public ResponseEntity<?> deleteNetworkOfCrack(
            @PathVariable String facility_id,
            @PathVariable String model_id,
            @PathVariable String crack_network_id) {

        if(!parsePathVariablesOfNetworkOfCrack(facility_id,model_id,crack_network_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = networkOfCrackService.deleteEntity(n_c_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_crack_network))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
