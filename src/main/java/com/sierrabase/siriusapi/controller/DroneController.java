package com.sierrabase.siriusapi.controller;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.entity.DroneTypeEntity;
import com.sierrabase.siriusapi.model.DroneSystemLogModel;
import com.sierrabase.siriusapi.model.DroneTypeModel;
import com.sierrabase.siriusapi.model.FacilityModel;
import com.sierrabase.siriusapi.service.DroneService;
import com.sierrabase.siriusapi.service.DroneSystemLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/drones")
public class DroneController {
    static private final String apiTag = "/api/drones";

    @Autowired
    private DroneService droneService;
    @Autowired
    private DroneSystemLogService droneSystemLogService;

    static private final String uri_types = "/types";
    static private final String uri_logs = "/logs";
    static private final String uri_drone_type_id = uri_types + "/{drone_type_id}";
    static private final String uri_drone_system_log_id = uri_logs + "/{drone_system_log_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int d_id;
    private boolean parsePathVariablesOfDroneType(String droneId) {
        d_id = URIParser.parseStringToIntegerId(droneId);
        if(d_id < 0)
            return false;
        return true;
    }


    // GET - drone types
    @GetMapping(uri_types)
    public ResponseEntity<?> getDroneTypes() {
        ArrayList<DroneTypeModel> droneTypeModelList = droneService.getAllEntities();
        log.info("model: " + droneTypeModelList);
        ResponseDTO<ArrayList<DroneTypeModel>> response = ResponseDTO.<ArrayList<DroneTypeModel>>builder()
                .uri(getUri(uri_types))
                .success(droneTypeModelList != null)
                .result(droneTypeModelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a droneType
    @GetMapping(uri_drone_type_id)
    public ResponseEntity<?> getDroneTypeById(
            @PathVariable(required=true)String drone_type_id)
    {
        if(!parsePathVariablesOfDroneType(drone_type_id))
            return ResponseEntity.badRequest().build();

        DroneTypeModel droneType = droneService.getEntityById(d_id);
        log.info("model: " + droneType);
        ResponseDTO<DroneTypeModel> response = ResponseDTO.<DroneTypeModel>builder()
                .uri(getUri(uri_drone_type_id))
                .success(droneType != null)
                .result(droneType)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a droneType
    @PostMapping(uri_types)
    public ResponseEntity<?> createDroneType(
            @RequestBody DroneTypeModel droneTypeModel) {
        DroneTypeModel createdDroneType = droneService.createEntity(droneTypeModel);
        log.info("model: " + createdDroneType);
        ResponseDTO<DroneTypeModel> response = ResponseDTO.<DroneTypeModel>builder()
                .uri(getUri(""))
                .success(createdDroneType != null)
                .result(createdDroneType)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a droneType
    @PutMapping(uri_drone_type_id)
    public ResponseEntity<?> updateDroneType(
            @PathVariable(required=true)String drone_type_id,
            @RequestBody DroneTypeModel droneTypeModel) {

        if(!parsePathVariablesOfDroneType(drone_type_id))
            return ResponseEntity.badRequest().build();
        DroneTypeModel updatedDroneType = droneService.updateEntity(d_id, droneTypeModel);
        log.info("model: " + updatedDroneType);
        ResponseDTO<DroneTypeModel> response = ResponseDTO.<DroneTypeModel>builder()
                .uri(getUri(uri_drone_type_id))
                .success(updatedDroneType != null)
                .result(updatedDroneType)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a droneType
    @DeleteMapping(uri_drone_type_id)
    public ResponseEntity<?> deleteDroneType(@PathVariable(required=true)String drone_type_id) {
        if(!parsePathVariablesOfDroneType(drone_type_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = droneService.deleteEntity(d_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_drone_type_id))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - drone types
    @GetMapping(uri_logs)
    public ResponseEntity<?> getDroneSystemLogs() {
        ArrayList<DroneSystemLogModel> droneSystemLogModelList = droneSystemLogService.getAllEntities();
        log.info("model: " + droneSystemLogModelList);
        ResponseDTO<ArrayList<DroneSystemLogModel>> response = ResponseDTO.<ArrayList<DroneSystemLogModel>>builder()
                .uri(getUri(uri_logs))
                .success(droneSystemLogModelList != null)
                .result(droneSystemLogModelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a droneType
    @GetMapping(uri_drone_system_log_id)
    public ResponseEntity<?> getDroneSystemLogById(
            @PathVariable Integer drone_system_log_id)
    {
        DroneSystemLogModel model = droneSystemLogService.getEntityById(drone_system_log_id);
        log.info("model: " + model);
        ResponseDTO<DroneSystemLogModel> response = ResponseDTO.<DroneSystemLogModel>builder()
                .uri(getUri(uri_drone_system_log_id))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a droneType
    @PostMapping(uri_logs)
    public ResponseEntity<?> createDroneSystemLog(
            @RequestBody DroneSystemLogModel droneSystemLogModel) {
        DroneSystemLogModel model = droneSystemLogService.createEntity(droneSystemLogModel);
        log.info("model: " + model);
        ResponseDTO<DroneSystemLogModel> response = ResponseDTO.<DroneSystemLogModel>builder()
                .uri(getUri(""))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a droneType
    @PutMapping(uri_drone_system_log_id)
    public ResponseEntity<?> updateDroneSystemLog(
            @PathVariable Integer drone_system_log_id,
            @RequestBody DroneSystemLogModel droneSystemLogModel) {

        DroneSystemLogModel updatedDroneSystemModel = droneSystemLogService.updateEntity(drone_system_log_id, droneSystemLogModel);
        log.info("model: " + updatedDroneSystemModel);
        ResponseDTO<DroneSystemLogModel> response = ResponseDTO.<DroneSystemLogModel>builder()
                .uri(getUri(uri_drone_system_log_id))
                .success(updatedDroneSystemModel != null)
                .result(updatedDroneSystemModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a droneType
    @DeleteMapping(uri_drone_system_log_id)
    public ResponseEntity<?> deleteDroneSystemLog(@PathVariable Integer drone_system_log_id) {

        boolean deleted = droneSystemLogService.deleteEntity(drone_system_log_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_drone_type_id))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
