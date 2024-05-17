package com.sierrabase.siriusapi.controller.mapping;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.mapping.MappingMissionModel;
import com.sierrabase.siriusapi.model.mapping.MappingMissionWaypointsModel;
import com.sierrabase.siriusapi.service.mapping.MappingMissionService;
import com.sierrabase.siriusapi.service.mapping.MappingMissionWaypointsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Slf4j
@RestController
@RequestMapping("/api/mapping")
public class MappingController {

    static private final String apiTag = "/api/mapping";

    @Autowired
    private MappingMissionService mappingMissionService;

    static private final String uri_facility = "/facilities/{facility_id}";
    static private final String uri_facility_maps = uri_facility + "/maps/{facility_map_id}";
    static private final String uri_missions = uri_facility_maps + "/missions";
    static private final String uri_mission = uri_missions + "/{mapping_mission_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int f_id, f_map_id, m_id;

    private boolean parsePathVariablesOfFacility(String facility_id) {
        f_id = URIParser.parseStringToIntegerId(facility_id);
        if (f_id < 0)
            return false;

        return true;
    }

    private boolean parsePathVariablesOfFacilityMap(String facility_id, String facility_map_id) {
        if(!parsePathVariablesOfFacility(facility_id))
            return false;

        f_map_id = URIParser.parseStringToIntegerId(facility_map_id);
        if (f_map_id < 0)
            return false;

        return true;
    }

    private boolean parsePathVariablesOfMission(String facility_id, String facility_map_id, String mapping_mission_id) {
        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return false;

        m_id = URIParser.parseStringToIntegerId(mapping_mission_id);
        if (m_id < 0)
            return false;

        return true;
    }

    // GET - missions of a map about a facility
    @GetMapping(uri_missions)
    public ResponseEntity<?> getMissions(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id) {

        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        ArrayList<MappingMissionModel> mappingMissionList = mappingMissionService.getAllEntitiesByFacilityMapId(f_map_id);
        log.info("model: " + mappingMissionList);
        ResponseDTO<ArrayList<MappingMissionModel>> response = ResponseDTO.<ArrayList<MappingMissionModel>>builder()
                .uri(getUri(uri_missions))
                .success(mappingMissionList != null)
                .result(mappingMissionList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a mission of a map about a facility
    @GetMapping(uri_mission)
    public ResponseEntity<?> getMission(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String mapping_mission_id) {

        if(!parsePathVariablesOfMission(facility_id, facility_map_id, mapping_mission_id))
            return ResponseEntity.badRequest().build();

        MappingMissionModel mappingMission = mappingMissionService.getEntityById(m_id);
        log.info("model: " + mappingMission);
        ResponseDTO<MappingMissionModel> response = ResponseDTO.<MappingMissionModel>builder()
                .uri(getUri(uri_mission))
                .success(mappingMission != null)
                .result(mappingMission)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a new mission of a map about a facility
    @PostMapping(uri_missions)
    public ResponseEntity<?> createMission(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @RequestBody MappingMissionModel missionModel) {

        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();
        log.info("test :" + missionModel);
        MappingMissionModel createdMappingMission = mappingMissionService.createEntity(missionModel);
        log.info("model: " + createdMappingMission);
        ResponseDTO<MappingMissionModel> response = ResponseDTO.<MappingMissionModel>builder()
                .uri(getUri(uri_missions))
                .success(createdMappingMission != null)
                .result(createdMappingMission)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a mission of a map about a facility
    @PutMapping(uri_mission)
    public ResponseEntity<?> updateMission(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String mapping_mission_id,
            @RequestBody MappingMissionModel missionModel) {

        if(!parsePathVariablesOfMission(facility_id, facility_map_id, mapping_mission_id))
            return ResponseEntity.badRequest().build();

        MappingMissionModel updatedMappingMission = mappingMissionService.updateEntity(m_id, missionModel);
        log.info("model: " + updatedMappingMission);
        ResponseDTO<MappingMissionModel> response = ResponseDTO.<MappingMissionModel>builder()
                .uri(getUri(uri_mission))
                .success(updatedMappingMission != null)
                .result(updatedMappingMission)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a mission of a map about a facility
    @DeleteMapping(uri_mission)
    public ResponseEntity<?> deleteMission(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String mapping_mission_id) {

        if(!parsePathVariablesOfMission(facility_id, facility_map_id, mapping_mission_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = mappingMissionService.deleteEntity(m_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_mission))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @Autowired
    private MappingMissionWaypointsService mappingMissionWaypointsService;

    static private final String uri_waypoints = uri_mission + "/waypoints";

    // GET - get waypoints of a imp
    @GetMapping(uri_waypoints)
    public ResponseEntity<?> getWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String mapping_mission_id) {

        if(!parsePathVariablesOfMission(facility_id, facility_map_id, mapping_mission_id))
            return ResponseEntity.badRequest().build();

        ArrayList<MappingMissionWaypointsModel> waypoints = mappingMissionWaypointsService.getAllEntitiesByMappingMissionId(m_id);
        log.info("model: " + waypoints);
        ResponseDTO<ArrayList<MappingMissionWaypointsModel>> response = ResponseDTO.<ArrayList<MappingMissionWaypointsModel>>builder()
                .uri(getUri(uri_waypoints))
                .success(waypoints != null)
                .result(waypoints)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - new waypoints of a imp
    @PostMapping(uri_waypoints)
    public ResponseEntity<?> createWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String mapping_mission_id,
            @RequestBody ArrayList<MappingMissionWaypointsModel> mappingMissionWaypointsModelArrayList) {

        if(!parsePathVariablesOfMission(facility_id, facility_map_id, mapping_mission_id))
            return ResponseEntity.badRequest().build();

        log.info("list: " + mappingMissionWaypointsModelArrayList);
        ArrayList<MappingMissionWaypointsModel> createdWaypoints = mappingMissionWaypointsService.createEntities(mappingMissionWaypointsModelArrayList);
        log.info("model: " + createdWaypoints);
        ResponseDTO<ArrayList<MappingMissionWaypointsModel> > response = ResponseDTO.<ArrayList<MappingMissionWaypointsModel> >builder()
                .uri(getUri(uri_waypoints))
                .success(createdWaypoints != null)
                .result(createdWaypoints)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - waypoints of a imp
    @PutMapping(uri_waypoints)
    public ResponseEntity<?> updateWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String mapping_mission_id,
            @RequestBody ArrayList<MappingMissionWaypointsModel> mappingMissionWaypointsModelArrayList) {

        if(!parsePathVariablesOfMission(facility_id, facility_map_id, mapping_mission_id))
            return ResponseEntity.badRequest().build();

        ArrayList<MappingMissionWaypointsModel> updateWaypoints = mappingMissionWaypointsService.updateEntities(m_id, mappingMissionWaypointsModelArrayList);
        log.info("model: " + updateWaypoints);
        ResponseDTO<ArrayList<MappingMissionWaypointsModel> > response = ResponseDTO.<ArrayList<MappingMissionWaypointsModel> >builder()
                .uri(getUri(uri_waypoints))
                .success(updateWaypoints != null)
                .result(updateWaypoints)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - waypoints of a imp
    @DeleteMapping(uri_waypoints)
    public ResponseEntity<?> deleteFacility(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String mapping_mission_id) {

        if(!parsePathVariablesOfMission(facility_id, facility_map_id, mapping_mission_id))
            return ResponseEntity.badRequest().build();

        Integer deleted = mappingMissionWaypointsService.deleteEntities(m_id);
        log.info("model: " + deleted);
        ResponseDTO<Integer> response = ResponseDTO.<Integer>builder()
                .uri(getUri(uri_waypoints))
                .success(deleted >= 1)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
