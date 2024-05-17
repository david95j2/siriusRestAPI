package com.sierrabase.siriusapi.controller.inspection.shapes;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.inspection.IMPModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGRectangleModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGRectangleService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeCommonService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/inspection/shapes/rectangles")
public class IMPGRectangleController {
    static private final String apiTag = "/api/inspection/shapes/rectangles";
    static private final String uri_rectangle = "/{impg_rectangle_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    @Autowired
    private IMPGShapeService impgShapeService;

    private int r_id;
    private boolean parsePathVariablesOfShapes(String shapeId) {
        r_id = URIParser.parseStringToIntegerId(shapeId);
        if(r_id < 0)
            return false;
        return true;
    }

    @Autowired
    private IMPGRectangleService impgRectangleService;
    @Autowired
    private IMPGShapeCommonService impgShapeCommonService;

    // GET - information of a rectangle
    @GetMapping(uri_rectangle)
    public ResponseEntity<?> getRectangle(
            @PathVariable(required=true)String impg_rectangle_id) {

        if(!parsePathVariablesOfShapes(impg_rectangle_id))
            return ResponseEntity.badRequest().build();

        IMPGRectangleModel impgRectangle = impgRectangleService.getEntityById(r_id);

        ResponseDTO<IMPGRectangleModel> response = ResponseDTO.<IMPGRectangleModel>builder()
                .uri(getUri(uri_rectangle))
                .success(impgRectangle != null)
                .result(impgRectangle)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - information of a rectangle
    @PostMapping()
    public ResponseEntity<?> postRectangle(
            @RequestBody IMPGRectangleModel impgRectangleModel) {

        if(!parsePathVariablesOfShapes(String.valueOf(impgRectangleModel.getShape().getImpgId())))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.createEntity(impgRectangleModel.getShape());
        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgRectangleModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.createEntity(shapeModel, impgRectangleModel.getCommonProperty());
        impgRectangleModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGRectangleModel createdImpgRectangle = impgRectangleService.createEntity(impgRectangleModel);
//        log.info("model: " + createdImpgRectangle);
        ResponseDTO<IMPGRectangleModel> response = ResponseDTO.<IMPGRectangleModel>builder()
                .uri(getUri(apiTag))
                .success(createdImpgRectangle != null)
                .result(createdImpgRectangle)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - information of a rectangle
    @PutMapping(uri_rectangle)
    public ResponseEntity<?> updateRectangle(@PathVariable(required = true) String impg_rectangle_id, @RequestBody IMPGRectangleModel impgRectangleModel) {

        if(!parsePathVariablesOfShapes(impg_rectangle_id))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.updateEntity(r_id,impgRectangleModel.getShape());
        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgRectangleModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.updateEntity(shapeModel, impgRectangleModel.getCommonProperty());
        impgRectangleModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGRectangleModel updateImpgRectangle = impgRectangleService.updateEntity(r_id, impgRectangleModel);
        log.info("model: " + updateImpgRectangle);
        ResponseDTO<IMPGRectangleModel> response = ResponseDTO.<IMPGRectangleModel>builder()
                .uri(getUri(uri_rectangle))
                .success(updateImpgRectangle != null)
                .result(updateImpgRectangle)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // Delete - information of a rectangle
    @DeleteMapping(uri_rectangle)
    public ResponseEntity<?> deleteRectangle(@PathVariable(required = true) String impg_rectangle_id) {
        if(!parsePathVariablesOfShapes(impg_rectangle_id)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deletedImpgsc = impgShapeCommonService.deleteEntity(r_id);
        boolean deleted = impgRectangleService.deleteEntity(r_id);
        boolean deletedImpgs = impgShapeService.deleteEntity(r_id);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_rectangle))
                .success(deletedImpgsc && deleted && deletedImpgs)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    /*
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
    */
}
