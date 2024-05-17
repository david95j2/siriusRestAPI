package com.sierrabase.siriusapi.controller.inspection.shapes;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;

import com.sierrabase.siriusapi.model.inspection.shapes.IMPGLineModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeCommonPropertyModel;
import com.sierrabase.siriusapi.model.inspection.shapes.IMPGShapeModel;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGLineService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeCommonService;
import com.sierrabase.siriusapi.service.inspection.shapes.IMPGShapeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/inspection/shapes/lines")
public class IMPGLineController {
    static private final String apiTag = "/api/inspection/shapes/lines";
    static private final String uri_line = "/{impg_line_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    @Autowired
    private IMPGShapeService impgShapeService;

    private int l_id;
    private boolean parsePathVariablesOfShapes(String shapeId) {
        l_id = URIParser.parseStringToIntegerId(shapeId);
        if(l_id < 0)
            return false;
        return true;
    }

    @Autowired
    private IMPGLineService impgLineService;
    @Autowired
    private IMPGShapeCommonService impgShapeCommonService;

    // GET - information of a line
    @GetMapping(uri_line)
    public ResponseEntity<?> getLine(
            @PathVariable(required=true)String impg_line_id) {

        if(!parsePathVariablesOfShapes(impg_line_id))
            return ResponseEntity.badRequest().build();

        IMPGLineModel impgLineModel = impgLineService.getEntityById(l_id);

        ResponseDTO<IMPGLineModel> response = ResponseDTO.<IMPGLineModel>builder()
                .uri(getUri(uri_line))
                .success(impgLineModel != null)
                .result(impgLineModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - information of a line
    @PostMapping()
    public ResponseEntity<?> postLine(
            @RequestBody IMPGLineModel impgLineModel) {

        if(!parsePathVariablesOfShapes(String.valueOf(impgLineModel.getShape().getImpgId())))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.createEntity(impgLineModel.getShape());
        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgLineModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.createEntity(shapeModel, impgLineModel.getCommonProperty());
        impgLineModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGLineModel createdImpgLine = impgLineService.createEntity(impgLineModel);
//        log.info("model: " + createdImpgLine);
        ResponseDTO<IMPGLineModel> response = ResponseDTO.<IMPGLineModel>builder()
                .uri(getUri(apiTag))
                .success(createdImpgLine != null)
                .result(createdImpgLine)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - information of a line
    @PutMapping(uri_line)
    public ResponseEntity<?> updateLine(@PathVariable(required = true) String impg_line_id, @RequestBody IMPGLineModel impgLineModel) {

        if(!parsePathVariablesOfShapes(impg_line_id))
            return ResponseEntity.badRequest().build();

        IMPGShapeModel shapeModel = impgShapeService.updateEntity(l_id,impgLineModel.getShape());
        if(shapeModel == null)
            return ResponseEntity.badRequest().build();
        impgLineModel.setShape(shapeModel);

        IMPGShapeCommonPropertyModel impgShapeCommonPropertyModel = impgShapeCommonService.updateEntity(shapeModel, impgLineModel.getCommonProperty());
        impgLineModel.setCommonProperty(impgShapeCommonPropertyModel);

        IMPGLineModel updateImpgLine = impgLineService.updateEntity(l_id, impgLineModel);
        log.info("model: " + updateImpgLine);
        ResponseDTO<IMPGLineModel> response = ResponseDTO.<IMPGLineModel>builder()
                .uri(getUri(uri_line))
                .success(updateImpgLine != null)
                .result(updateImpgLine)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // Delete - information of a line
    @DeleteMapping(uri_line)
    public ResponseEntity<?> deleteLine(@PathVariable(required = true) String impg_line_id) {
        if(!parsePathVariablesOfShapes(impg_line_id)) {
            return ResponseEntity.badRequest().build();
        }
        boolean deletedImpgsc = impgShapeCommonService.deleteEntity(l_id);
        boolean deleted = impgLineService.deleteEntity(l_id);
        boolean deletedImpgs = impgShapeService.deleteEntity(l_id);

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_line))
                .success(deletedImpgsc && deleted && deletedImpgs)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
