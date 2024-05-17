package com.sierrabase.siriusapi.controller.inspection.shapes;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGCircleModel;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGCircleService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeCommonService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/inspection/shapes/circles")
public class IMPGCircleController {
    static private final String apiTag = "/api/inspection/shapes/circles";
    static private final String uri_circle = "/{impg_circle_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    @Autowired
    private IMPGShapeService impgShapeService;

    private int c_id;
    private boolean parsePathVariablesOfShapes(String shapeId) {
        c_id = URIParser.parseStringToIntegerId(shapeId);
        if(c_id < 0)
            return false;
        return true;
    }

    @Autowired
    private IMPGCircleService impgCircleService;
    @Autowired
    private IMPGShapeCommonService impgShapeCommonService;

    // GET - information of a circle
    @GetMapping(uri_circle)
    public ResponseEntity<?> getCircle(
            @PathVariable(required=true)String impg_circle_id) {

        if(!parsePathVariablesOfShapes(impg_circle_id))
            return ResponseEntity.badRequest().build();

        IMPGCircleModel impgCircleModel = impgCircleService.getEntityById(c_id);

        ResponseDTO<IMPGCircleModel> response = ResponseDTO.<IMPGCircleModel>builder()
                .uri(getUri(uri_circle))
                .success(impgCircleModel != null)
                .result(impgCircleModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - information of a circle
    @PostMapping()
    public ResponseEntity<?> postCircle(
            @RequestBody IMPGCircleModel impgCircleModel) {
        if(!parsePathVariablesOfShapes(String.valueOf(impgCircleModel.getShape().getImpgId())))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.createEntity(impgCircleModel.getShape());
        if(shapeModel == null) {
            return ResponseEntity.badRequest().build();
        }
        impgCircleModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.createEntity(shapeModel, impgCircleModel.getCommonProperty());
        impgCircleModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGCircleModel createdImpgCircle = impgCircleService.createEntity(impgCircleModel);

//        log.info("model("+getUri(apiTag)+"): " + createdImpgCircle);
        ResponseDTO<IMPGCircleModel> response = ResponseDTO.<IMPGCircleModel>builder()
                .uri(getUri(apiTag))
                .success(createdImpgCircle != null)
                .result(createdImpgCircle)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - information of a circle
    @PutMapping(uri_circle)
    public ResponseEntity<?> updateCircle(@PathVariable(required = true) String impg_circle_id, @RequestBody IMPGCircleModel impgCircleModel) {

        if(!parsePathVariablesOfShapes(impg_circle_id))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.updateEntity(c_id,impgCircleModel.getShape());
        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgCircleModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.updateEntity(shapeModel, impgCircleModel.getCommonProperty());
        impgCircleModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGCircleModel updateImpgCircle = impgCircleService.updateEntity(c_id, impgCircleModel);
        log.info("model: " + updateImpgCircle);
        ResponseDTO<IMPGCircleModel> response = ResponseDTO.<IMPGCircleModel>builder()
                .uri(getUri(uri_circle))
                .success(updateImpgCircle != null)
                .result(updateImpgCircle)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // Delete - information of a circle
    @DeleteMapping(uri_circle)
    public ResponseEntity<?> deleteCircle(@PathVariable(required = true) String impg_circle_id) {
        if(!parsePathVariablesOfShapes(impg_circle_id)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deletedImpgsc = impgShapeCommonService.deleteEntity(c_id);
        boolean deleted = impgCircleService.deleteEntity(c_id);
        boolean deletedImpgs = impgShapeService.deleteEntity(c_id);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_circle))
                .success(deletedImpgsc && deleted && deletedImpgs)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
