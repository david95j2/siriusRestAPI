package com.sierrabase.siriusapi.controller.inspection.shapes;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGAbutmentModel;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGAbutmentService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeCommonService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/inspection/shapes/abutments")
public class IMPGAbutmentController {
    static private final String apiTag = "/api/inspection/shapes/abutments";
    static private final String uri_abutment = "/{impg_abutment_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    @Autowired
    private IMPGShapeService impgShapeService;

    private int a_id;
    private boolean parsePathVariablesOfShapes(String shapeId) {
        a_id = URIParser.parseStringToIntegerId(shapeId);
        if(a_id < 0)
            return false;
        return true;
    }

    @Autowired
    private IMPGAbutmentService impgAbutmentService;
    @Autowired
    private IMPGShapeCommonService impgShapeCommonService;

    // GET - information of a abutment
    @GetMapping(uri_abutment)
    public ResponseEntity<?> getAbutment(
            @PathVariable(required=true)String impg_abutment_id) {

        if(!parsePathVariablesOfShapes(impg_abutment_id))
            return ResponseEntity.badRequest().build();

        IMPGAbutmentModel impgAbutmentModel = impgAbutmentService.getEntityById(a_id);

        ResponseDTO<IMPGAbutmentModel> response = ResponseDTO.<IMPGAbutmentModel>builder()
                .uri(getUri(uri_abutment))
                .success(impgAbutmentModel != null)
                .result(impgAbutmentModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - information of a abutment
    @PostMapping()
    public ResponseEntity<?> postAbutment(
            @RequestBody IMPGAbutmentModel impgAbutmentModel) {

        if(!parsePathVariablesOfShapes(String.valueOf(impgAbutmentModel.getShape().getImpgId())))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.createEntity(impgAbutmentModel.getShape());
        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgAbutmentModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.createEntity(shapeModel, impgAbutmentModel.getCommonProperty());
        impgAbutmentModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGAbutmentModel createdImpgAbutment = impgAbutmentService.createEntity(impgAbutmentModel);
//        log.info("model: " + createdImpgAbutment);
        ResponseDTO<IMPGAbutmentModel> response = ResponseDTO.<IMPGAbutmentModel>builder()
                .uri(getUri(apiTag))
                .success(createdImpgAbutment != null)
                .result(createdImpgAbutment)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - information of a abutment
    @PutMapping(uri_abutment)
    public ResponseEntity<?> updateAbutment(@PathVariable(required = true) String impg_abutment_id, @RequestBody IMPGAbutmentModel impgAbutmentModel) {

        if(!parsePathVariablesOfShapes(impg_abutment_id))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.updateEntity(a_id,impgAbutmentModel.getShape());
        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgAbutmentModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.updateEntity(shapeModel, impgAbutmentModel.getCommonProperty());
        impgAbutmentModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGAbutmentModel updateImpgAbutment = impgAbutmentService.updateEntity(a_id, impgAbutmentModel);
        log.info("model: " + updateImpgAbutment);
        ResponseDTO<IMPGAbutmentModel> response = ResponseDTO.<IMPGAbutmentModel>builder()
                .uri(getUri(uri_abutment))
                .success(updateImpgAbutment != null)
                .result(updateImpgAbutment)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // Delete - information of a abutment
    @DeleteMapping(uri_abutment)
    public ResponseEntity<?> deleteAbutment(@PathVariable(required = true) String impg_abutment_id) {
        if(!parsePathVariablesOfShapes(impg_abutment_id)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deletedImpgsc = impgShapeCommonService.deleteEntity(a_id);
        boolean deleted = impgAbutmentService.deleteEntity(a_id);
        boolean deletedImpgs = impgShapeService.deleteEntity(a_id);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_abutment))
                .success(deletedImpgsc && deleted && deletedImpgs)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
