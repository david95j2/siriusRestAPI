package com.sierrabase.siriusapi.controller.inspection;

import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.inspection.*;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.service.inspection.*;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeService;
import com.sierrabase.siriusapi.service.worker.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/inspection")
public class InspectionController {

    static private final String apiTag = "/api/inspection";

    static private final String uri_facility = "/facilities/{facility_id}";
    static private final String uri_facility_maps = uri_facility + "/maps/{facility_map_id}";
    static private final String uri_fitArea = uri_facility_maps + "/fitting";
    static private final String uri_imps = uri_facility_maps + "/missions";
    static private final String uri_imp = uri_imps + "/{imp_id}";
    static private final String uri_imp_waypoints = uri_imp + "/waypoints";

    static private final String uri_imp_waypoints_gimbal = uri_imp_waypoints + "/gimbal";
    static private final String uri_imp_groups = uri_imp + "/groups";
    static private final String uri_imp_group = uri_imp_groups + "/{impg_id}";
    static private final String uri_imp_group_waypoints = uri_imp_group + "/waypoints";

    static private final String uri_impg_shapes = uri_imp + "/shapes";
    static private final String uri_impg_shape = uri_impg_shapes + "/{impg_shape_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int f_id, f_m_id, i_id, ig_id, is_id;
    private boolean parsePathVariablesOfFacility(String facilityId) {
        f_id = URIParser.parseStringToIntegerId(facilityId);
        if(f_id < 0)
            return false;
        return true;
    }
    private boolean parsePathVariablesOfFacilityMap(String facilityId, String facilityMapId) {
        if(!parsePathVariablesOfFacility(facilityId))
            return false;
        f_m_id = URIParser.parseStringToIntegerId(facilityMapId);
        if(f_m_id < 0)
            return false;
        return true;
    }
    private boolean parsePathVariablesOfImp(String facilityId, String facilityMapId, String impId) {
        if(!parsePathVariablesOfFacilityMap(facilityId, facilityMapId))
            return false;

        i_id = URIParser.parseStringToIntegerId(impId);

        if(i_id < 0)
            return false;

        return true;
    }
    private boolean parsePathVariablesOfImpg(String facilityId, String facilityMapId, String impId, String impgId) {
        if(!parsePathVariablesOfImp(facilityId, facilityMapId, impId))
            return false;

        ig_id = URIParser.parseStringToIntegerId(impgId);
        if(ig_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfImpShapes(String facilityId, String facilityMapId, String impId, String impShapeId) {
        if(!parsePathVariablesOfImp(facilityId, facilityMapId, impId))
            return false;

        is_id = URIParser.parseStringToIntegerId(impShapeId);
        if(is_id < 0)
            return false;
        return true;
    }

    @Autowired
    private IMPService impService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private ImpWorker impWorker;

    // GET - imp_missions of a map about a facility
    @GetMapping(uri_imps)
    public ResponseEntity<?> getMissions(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id) {

        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPModel> impModelList = impService.getAllEntitiesByFacilityMapId(f_m_id);
        log.info("model: " + impModelList);
        ResponseDTO<ArrayList<IMPModel>> response = ResponseDTO.<ArrayList<IMPModel>>builder()
                .uri(getUri(uri_imps))
                .success(impModelList != null)
                .result(impModelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a imp_mission of a map about a facility
    @GetMapping(uri_imp)
    public ResponseEntity<?> getMissions(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id) {

        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        IMPModel imp = impService.getEntityById(i_id);
        log.info("model: " + imp);
        ResponseDTO<IMPModel> response = ResponseDTO.<IMPModel>builder()
                .uri(getUri(uri_imp))
                .success(imp != null)
                .result(imp)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a imp_mission of a map about a facility
    @PostMapping(uri_imps)
    public ResponseEntity<?> createMission(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @RequestBody IMPModel impModel)
    {
        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        IMPModel createdImp = impService.createEntity(impModel);
        log.info("model: " + createdImp);
        ResponseDTO<IMPModel> response = ResponseDTO.<IMPModel>builder()
                .uri(getUri(uri_imps))
                .success(createdImp != null)
                .result(createdImp)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(uri_fitArea)
    public ResponseEntity<?> createPathPlanning(
            @PathVariable String facility_id,
            @PathVariable String facility_map_id,
            @RequestBody FitAreaProgramModel model)
    {
        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        boolean result = impService.createPathPlanning(f_id,f_m_id,model);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_fitArea))
                .success(true)
                .result(true)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a imp_mission of a map about a facility
    @PutMapping(uri_imp)
    public ResponseEntity<?> updateMission(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @RequestBody IMPModel impModel)
    {
        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        IMPModel updateImp = impService.updateEntity(i_id, impModel);
        log.info("model: " + updateImp);
        ResponseDTO<IMPModel> response = ResponseDTO.<IMPModel>builder()
                .uri(getUri(uri_imp))
                .success(updateImp != null)
                .result(updateImp)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a imp_mission of a map about a facility
    @DeleteMapping(uri_imp)
    public ResponseEntity<?> deleteMap(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id)
    {
        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = impService.deleteEntity(i_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_imp))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @Autowired
    private IMPGService impgService;

    // GET - imp_groups of a imp
    @GetMapping(uri_imp_groups)
    public ResponseEntity<?> getGroups(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id) {

        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))

            return ResponseEntity.badRequest().build();

        ArrayList<IMPGModel> impgList = impgService.getAllEntitiesByImpId(i_id);
        log.info("model: " + impgList);
        ResponseDTO<ArrayList<IMPGModel>> response = ResponseDTO.<ArrayList<IMPGModel>>builder()
                .uri(getUri(uri_imp_groups))
                .success(impgList != null)
                .result(impgList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a imp_group of a imp
    @GetMapping(uri_imp_group)
    public ResponseEntity<?> getGroup(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_id) {

        if(!parsePathVariablesOfImpg(facility_id, facility_map_id, imp_id, impg_id))
            return ResponseEntity.badRequest().build();

        IMPGModel impg = impgService.getEntityById(ig_id);
        log.info("model: " + impg);
        ResponseDTO<IMPGModel> response = ResponseDTO.<IMPGModel>builder()
                .uri(getUri(uri_imp_group))
                .success(impg != null)
                .result(impg)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a new imp_group of a imp
    @PostMapping(uri_imp_groups)
    public ResponseEntity<?> createdImpg(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @RequestBody IMPGModel impgModel) {

        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();
        IMPGModel createdImpg = impgService.createEntity(impgModel);
//        log.info("model("+getUri(uri_imp_groups) + "): " + createdImpg);
        ResponseDTO<IMPGModel> response = ResponseDTO.<IMPGModel>builder()
                .uri(getUri(uri_imp_groups))
                .success(createdImpg != null)
                .result(createdImpg)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a imp_group of a imp
    @PutMapping(uri_imp_group)
    public ResponseEntity<?> updateImpg(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_id,
            @RequestBody IMPGModel impgModel)     {

        if(!parsePathVariablesOfImpg(facility_id, facility_map_id, imp_id, impg_id))
            return ResponseEntity.badRequest().build();

        IMPGModel updateImpg = impgService.updateEntity(ig_id, impgModel);
        log.info("model: " + updateImpg);
        ResponseDTO<IMPGModel> response = ResponseDTO.<IMPGModel>builder()
                .uri(getUri(uri_imp_group))
                .success(updateImpg != null)
                .result(updateImpg)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a imp_group of a imp
    @DeleteMapping(uri_imp_group)
    public ResponseEntity<?> deleteImpg(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_id) {

        if(!parsePathVariablesOfImpg(facility_id, facility_map_id, imp_id, impg_id))
            return ResponseEntity.badRequest().build();

        Boolean deleted = impgService.deleteEntity(ig_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_imp_group))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @Autowired
    private IMPWpsService impWpsService;
    @Autowired
    private IMPGWpsService impgWpsService;
    @Autowired
    private IMPWpsMissionCameraService impWpsMissionCameraService;

    // GET - imp_waypoints of a imp
    @GetMapping(uri_imp_waypoints)
    public ResponseEntity<?> getImpWaypoints(
            @PathVariable String facility_id,
            @PathVariable String facility_map_id,
            @PathVariable String imp_id) {

        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();
        ArrayList<IMPWpsModel> impWpsList = impWpsService.getAllEntitiesByImpId(i_id);
        log.info("model: " + impWpsList);
        ResponseDTO<ArrayList<IMPWpsModel>> response = ResponseDTO.<ArrayList<IMPWpsModel>>builder()
                .uri(getUri(uri_imp_waypoints))
                .success(impWpsList != null)
                .result(impWpsList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - new imp_waypoints of a imp
    @PostMapping(uri_imp_waypoints)
    public ResponseEntity<?> createdImpWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @RequestBody ArrayList<IMPWpsModel> impWpsModelArrayList) {

        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPWpsModel> createdImpWps = impWpsService.createEntities(impWpsModelArrayList);
        log.info("model: " + createdImpWps);
        ResponseDTO<ArrayList<IMPWpsModel> > response = ResponseDTO.<ArrayList<IMPWpsModel> >builder()
                .uri(getUri(uri_imp_waypoints))
                .success(createdImpWps != null)
                .result(createdImpWps)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - imp_waypoints of a imp
    @PutMapping(uri_imp_waypoints)
    public ResponseEntity<?> updateImpWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @RequestBody ArrayList<IMPWpsModel> impWpsModelArrayList)
    {
        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPWpsModel> updateImpWps = impWpsService.updateEntities(i_id, impWpsModelArrayList);
        log.info("model: " + updateImpWps);
        ResponseDTO<ArrayList<IMPWpsModel>> response = ResponseDTO.<ArrayList<IMPWpsModel>>builder()
                .uri(getUri(uri_imp_waypoints))
                .success(updateImpWps != null)
                .result(updateImpWps)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - imp_waypoints of a imp
    @DeleteMapping(uri_imp_waypoints)
    public ResponseEntity<?> deleteImpWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id)
    {
        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        Integer deleted = impWpsService.deleteEntities(i_id);
        log.info("model: " + deleted);
        ResponseDTO<Integer> response = ResponseDTO.<Integer>builder()
                .uri(getUri(uri_imp))
                .success(deleted >= 1)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - impg_waypoints of a impg
    @GetMapping(uri_imp_group_waypoints)
    public ResponseEntity<?> getImpgWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_id) {

        if(!parsePathVariablesOfImpg(facility_id, facility_map_id, imp_id, impg_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPWpsModel> impgWpsList = impgWpsService.getAllEntitiesByImpIdAndImpgId(i_id, ig_id);
        log.info("model: " + impgWpsList);
        ResponseDTO<ArrayList<IMPWpsModel>> response = ResponseDTO.<ArrayList<IMPWpsModel>>builder()
                .uri(getUri(uri_imp_group_waypoints))
                .success(impgWpsList != null)
                .result(impgWpsList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - new impg_waypoints of a impg
    @PostMapping(uri_imp_group_waypoints)
    public ResponseEntity<?> createdImpWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_id,
            @RequestBody ArrayList<IMPWpsModel> impWpsModelArrayList) {

        if(!parsePathVariablesOfImpg(facility_id, facility_map_id, imp_id, impg_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPWpsModel> createdImpWps = impWpsService.createEntities(impWpsModelArrayList);
        log.info("model: " + createdImpWps);
        ResponseDTO<ArrayList<IMPWpsModel> > response = ResponseDTO.<ArrayList<IMPWpsModel>>builder()
                .uri(getUri(uri_imp_group_waypoints))
                .success(createdImpWps != null)
                .result(createdImpWps)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - impg_waypoints of a impg
    @PutMapping(uri_imp_group_waypoints)
    public ResponseEntity<?> updateImpWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_id,
            @RequestBody ArrayList<IMPWpsModel> impWpsModelArrayList)
    {
        if(!parsePathVariablesOfImpg(facility_id, facility_map_id, imp_id, impg_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPWpsModel> updateImpgWps = impgWpsService.updateEntities(i_id, ig_id, impWpsModelArrayList);
        log.info("model: " + updateImpgWps);
        ResponseDTO<ArrayList<IMPWpsModel>> response = ResponseDTO.<ArrayList<IMPWpsModel>>builder()
                .uri(getUri(uri_imp_group_waypoints))
                .success(updateImpgWps != null)
                .result(updateImpgWps)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - impg_waypoints of a impg
    @DeleteMapping(uri_imp_group_waypoints)
    public ResponseEntity<?> deleteImpWaypoints(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_id)
    {
        if(!parsePathVariablesOfImpg(facility_id, facility_map_id, imp_id, impg_id))
            return ResponseEntity.badRequest().build();

        Integer deleted = impgWpsService.deleteEntities(i_id, ig_id);
        log.info("model: " + deleted);
        ResponseDTO<Integer> response = ResponseDTO.<Integer>builder()
                .uri(getUri(uri_imp_group_waypoints))
                .success(deleted >= 1)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @Autowired
    private IMPGShapeService impgShapeService;

    // GET - impg_shapes of a imp
    @GetMapping(uri_impg_shapes)
    public ResponseEntity<?> getShapes(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id) {


        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPGShapeModel> impgShapeList = impgShapeService.getAllEntitiesByImpId(i_id);
        log.info("model: " + impgShapeList);
        ResponseDTO<ArrayList<IMPGShapeModel>> response = ResponseDTO.<ArrayList<IMPGShapeModel>>builder()
                .uri(getUri(uri_impg_shapes))
                .success(impgShapeList != null)
                .result(impgShapeList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a impg_shape of a imp
    @GetMapping(uri_impg_shape)
    public ResponseEntity<?> getShape(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_shape_id) {

        if(!parsePathVariablesOfImpShapes(facility_id, facility_map_id, imp_id, impg_shape_id))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel imps = impgShapeService.getEntityById(is_id);
        log.info("model: " + imps);
        ResponseDTO<IMPGShapeModel> response = ResponseDTO.<IMPGShapeModel>builder()
                .uri(getUri(uri_impg_shape))
                .success(imps != null)
                .result(imps)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a new impg_shape of a imp
    @PostMapping(uri_impg_shapes)
    public ResponseEntity<?> createdImps(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @RequestBody IMPGShapeModel impgShapeModel) {

        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel createdImps = impgShapeService.createEntity(impgShapeModel);
//        log.info("model("+getUri(uri_impg_shapes)+"): " + createdImps);

        ResponseDTO<IMPGShapeModel> response = ResponseDTO.<IMPGShapeModel>builder()
                .uri(getUri(uri_impg_shapes))
                .success(createdImps != null)
                .result(createdImps)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a impg_shape of a imp
    @PutMapping(uri_impg_shape)
    public ResponseEntity<?> updateImps(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_shape_id,
            @RequestBody IMPGShapeModel impgShapeModel)     {

        if(!parsePathVariablesOfImpShapes(facility_id, facility_map_id, imp_id, impg_shape_id))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel updateImps = impgShapeService.updateEntity(is_id, impgShapeModel);
        log.info("model: " + updateImps);
        ResponseDTO<IMPGShapeModel> response = ResponseDTO.<IMPGShapeModel>builder()
                .uri(getUri(uri_impg_shape))
                .success(updateImps != null)
                .result(updateImps)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a impg_shape of a imp
    @DeleteMapping(uri_impg_shape)
    public ResponseEntity<?> deleteImps(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @PathVariable(required=true)String imp_id,
            @PathVariable(required=true)String impg_shape_id) {

        if(!parsePathVariablesOfImpShapes(facility_id, facility_map_id, imp_id, impg_shape_id))
            return ResponseEntity.badRequest().build();

        Boolean deleted = impgShapeService.deleteEntity(is_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_impg_shape))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }


    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    *
    *
    *
    *
    *
    *
    * */
    // GET - imp_waypoints of a imp
    @GetMapping(uri_imp_waypoints_gimbal)
    public ResponseEntity<?> getImpWaypointsWithGimbal(
            @PathVariable String facility_id,
            @PathVariable String facility_map_id,
            @PathVariable String imp_id) {

        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();
        ArrayList<IMPWpsMissionCameraModel> models = impWpsMissionCameraService.getAllEntitiesByImpId(i_id);
        log.info("models: " + models);
        ResponseDTO<ArrayList<IMPWpsMissionCameraModel>> response = ResponseDTO.<ArrayList<IMPWpsMissionCameraModel>>builder()
                .uri(getUri(uri_imp_waypoints_gimbal))
                .success(models != null)
                .result(models)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - new imp_waypoints of a imp
    @PostMapping(uri_imp_waypoints_gimbal)
    public ResponseEntity<?> createdImpWaypointsWithGimbal(
            @PathVariable String facility_id,
            @PathVariable String facility_map_id,
            @PathVariable String imp_id,
            @RequestBody ArrayList<IMPWpsMissionCameraModel> models) {

        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPWpsMissionCameraModel> createdModels = impWpsMissionCameraService.createEntities(models);
        log.info("models : " + createdModels);
        ResponseDTO<ArrayList<IMPWpsMissionCameraModel> > response = ResponseDTO.<ArrayList<IMPWpsMissionCameraModel> >builder()
                .uri(getUri(uri_imp_waypoints_gimbal))
                .success(createdModels != null)
                .result(createdModels)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - imp_waypoints of a imp
    @PutMapping(uri_imp_waypoints_gimbal)
    public ResponseEntity<?> updateImpWaypointsWithGimbal(
            @PathVariable String facility_id,
            @PathVariable String facility_map_id,
            @PathVariable String imp_id,
            @RequestBody ArrayList<IMPWpsMissionCameraModel> models)
    {
        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        ArrayList<IMPWpsMissionCameraModel> updatedModels = impWpsMissionCameraService.updateEntities(i_id, models);
        log.info("model: " + updatedModels);
        ResponseDTO<ArrayList<IMPWpsMissionCameraModel>> response = ResponseDTO.<ArrayList<IMPWpsMissionCameraModel>>builder()
                .uri(getUri(uri_imp_waypoints_gimbal))
                .success(updatedModels != null)
                .result(updatedModels)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - imp_waypoints of a imp
    @DeleteMapping(uri_imp_waypoints_gimbal)
    public ResponseEntity<?> deleteImpWaypointsWithGimbal(
            @PathVariable String facility_id,
            @PathVariable String facility_map_id,
            @PathVariable String imp_id)
    {
        if(!parsePathVariablesOfImp(facility_id, facility_map_id, imp_id))
            return ResponseEntity.badRequest().build();

        Integer deleted = impWpsMissionCameraService.deleteEntities(i_id);
        log.info("model: " + deleted);
        ResponseDTO<Integer> response = ResponseDTO.<Integer>builder()
                .uri(getUri(uri_imp_waypoints_gimbal))
                .success(deleted >= 1)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
