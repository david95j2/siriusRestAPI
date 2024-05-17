package com.sierrabase.siriusapi.controller.inspection.shapes;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGUndersideModel;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeCommonService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGUndersideService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/inspection/shapes/undersides")
public class IMPGUndersideController {
    static private final String apiTag = "/api/inspection/shapes/undersides";
    static private final String uri_underside = "/{impg_underside_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    @Autowired
    private IMPGShapeService impgShapeService;

    private int u_id;
    private boolean parsePathVariablesOfShapes(String shapeId) {
        u_id = URIParser.parseStringToIntegerId(shapeId);
        if(u_id < 0)
            return false;
        return true;
    }

    @Autowired
    private IMPGUndersideService impgUndersideService;
    @Autowired
    private IMPGShapeCommonService impgShapeCommonService;

    // GET - information of a underside
    @GetMapping(uri_underside)
    public ResponseEntity<?> getUnderside(
            @PathVariable(required=true)String impg_underside_id) {

        if(!parsePathVariablesOfShapes(impg_underside_id))
            return ResponseEntity.badRequest().build();

        IMPGUndersideModel impgUndersideModel = impgUndersideService.getEntityById(u_id);

        ResponseDTO<IMPGUndersideModel> response = ResponseDTO.<IMPGUndersideModel>builder()
                .uri(getUri(uri_underside))
                .success(impgUndersideModel != null)
                .result(impgUndersideModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - information of a underside
    @PostMapping()
    public ResponseEntity<?> postUnderside(
            @RequestBody IMPGUndersideModel impgUndersideModel) {

        if(!parsePathVariablesOfShapes(String.valueOf(impgUndersideModel.getShape().getImpgId())))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.createEntity(impgUndersideModel.getShape());

        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgUndersideModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.createEntity(shapeModel, impgUndersideModel.getCommonProperty());

        impgUndersideModel.setCommonProperty(impgShapeCommonPropertyModel);
        IMPGUndersideModel createdImpgUnderside = impgUndersideService.createEntity(impgUndersideModel);
//        log.info("model: " + createdImpgUnderside);
        ResponseDTO<IMPGUndersideModel> response = ResponseDTO.<IMPGUndersideModel>builder()
                .uri(getUri(apiTag))
                .success(createdImpgUnderside != null)
                .result(createdImpgUnderside)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - information of a underside
    @PutMapping(uri_underside)
    public ResponseEntity<?> updateUnderside(@PathVariable(required = true) String impg_underside_id, @RequestBody IMPGUndersideModel impgUndersideModel) {

        if(!parsePathVariablesOfShapes(impg_underside_id))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.updateEntity(u_id,impgUndersideModel.getShape());
        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgUndersideModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.updateEntity(shapeModel, impgUndersideModel.getCommonProperty());
        impgUndersideModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGUndersideModel updateImpgUnderside = impgUndersideService.updateEntity(u_id, impgUndersideModel);
        log.info("model: " + updateImpgUnderside);
        ResponseDTO<IMPGUndersideModel> response = ResponseDTO.<IMPGUndersideModel>builder()
                .uri(getUri(uri_underside))
                .success(updateImpgUnderside != null)
                .result(updateImpgUnderside)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // Delete - information of a underside
    @DeleteMapping(uri_underside)
    public ResponseEntity<?> deleteUnderside(@PathVariable(required = true) String impg_underside_id) {
        if(!parsePathVariablesOfShapes(impg_underside_id)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deletedImpgsc = impgShapeCommonService.deleteEntity(u_id);
        boolean deleted = impgUndersideService.deleteEntity(u_id);
        boolean deletedImpgs = impgShapeService.deleteEntity(u_id);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_underside))
                .success(deletedImpgsc && deleted && deletedImpgs)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
