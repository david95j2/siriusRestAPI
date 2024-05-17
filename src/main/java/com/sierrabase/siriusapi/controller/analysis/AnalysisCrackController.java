package com.sierrabase.siriusapi.controller.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.JsonInfoModel;
import com.sierrabase.siriusapi.model.analysis.JsonModel;
import com.sierrabase.siriusapi.service.album.AlbumPhotoService;
import com.sierrabase.siriusapi.service.album.AlbumService;
import com.sierrabase.siriusapi.service.analysis.AnalysisCrackService;
import com.sierrabase.siriusapi.service.analysis.AnalysisCrackWorker;
import com.sierrabase.siriusapi.service.worker.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class AnalysisCrackController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_analyses = uri_album + "/analyses";
    static private final String uri_analysis = uri_analyses + "/{analysis_id}";
    static private final String uri_analysis_cracks = uri_analysis + "/{analysis_type_name}";
    static private final String uri_analysis_crack = uri_analysis_cracks + "/{analysis_crack_id}";
    static private final String uri_analysis_crack_update = uri_analysis_crack +"/json";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int a_id, an_id, an_c_id;
    private String an_type_name;
    private boolean parsePathVariablesOfAlbum(String albumId) {
        a_id = URIParser.parseStringToIntegerId(albumId);
        if(a_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfAnalysis(String albumId, String analysisId) {
        if(!parsePathVariablesOfAlbum(albumId))
            return false;
        an_id = URIParser.parseStringToIntegerId(analysisId);
        if(an_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfAnalysisCrack(String albumId, String analysisId, String analysisCrackId) {
        if(!parsePathVariablesOfAnalysis(albumId,analysisId))
            return false;
        an_c_id = URIParser.parseStringToIntegerId(analysisCrackId);
        if(an_c_id < 0)
            return false;
        return true;
    }

    private boolean isValidTypeName(String typeName) {
        return typeName.equals("segmentations") || typeName.equals("detections") || typeName.equals("modelings");
    }

    @Autowired
    private AnalysisCrackService analysisCrackService;

    // GET - analysisCrack
    @GetMapping(uri_analysis_cracks)
    public ResponseEntity<?> getAnalysisCracks(@PathVariable String album_id,
                                              @PathVariable String analysis_id,
                                              @PathVariable String analysis_type_name) {

        if (!parsePathVariablesOfAnalysis(album_id,analysis_id) || !isValidTypeName(analysis_type_name))
            return ResponseEntity.badRequest().build();

        ArrayList<AnalysisCrackModel> modelList = analysisCrackService.getAllEntities(a_id, an_id);
//        log.info("model: " + modelList);
        ResponseDTO<ArrayList<AnalysisCrackModel>> response = ResponseDTO.<ArrayList<AnalysisCrackModel>>builder()
                .uri(getUri(uri_analysis_cracks))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a analysisCrack
    @GetMapping(uri_analysis_crack)
    public ResponseEntity<?> getAnalysisCrack(
            @PathVariable String album_id,
            @PathVariable String analysis_id,
            @PathVariable String analysis_type_name,
            @PathVariable String analysis_crack_id
            ) {

        if(!parsePathVariablesOfAnalysisCrack(album_id, analysis_id, analysis_crack_id) || !isValidTypeName(analysis_type_name))
            return ResponseEntity.badRequest().build();

        AnalysisCrackModel model = analysisCrackService.getEntityById(a_id, an_c_id);
//        log.info("model: " + model);
        ResponseDTO<AnalysisCrackModel> response = ResponseDTO.<AnalysisCrackModel>builder()
                .uri(getUri(uri_analysis_crack))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a analysisCrack
    @PostMapping(uri_analysis_cracks)
    public ResponseEntity<?> createAnalysisCrack(
            @PathVariable String album_id,
            @PathVariable String analysis_id,
            @PathVariable String analysis_type_name,
            @RequestBody AnalysisCrackModel model) {

        if(!parsePathVariablesOfAnalysis(album_id, analysis_id) || !isValidTypeName(analysis_type_name))
            return ResponseEntity.badRequest().build();

        AnalysisCrackModel createdModel = analysisCrackService.createEntity(model);
//        log.info("model: " + createdModel);
        ResponseDTO<AnalysisCrackModel> response = ResponseDTO.<AnalysisCrackModel>builder()
                .uri(getUri(uri_analysis_cracks))
                .success(createdModel != null)
                .result(createdModel)
                .build();
        return ResponseEntity.ok().body(response);
    }

    // PUT - a camera
    @PutMapping(uri_analysis_crack)
    public ResponseEntity<?> updateAnalysisCrack(
            @PathVariable String album_id,
            @PathVariable String analysis_id,
            @PathVariable String analysis_type_name,
            @PathVariable String analysis_crack_id,
            @RequestBody AnalysisCrackModel model)
    {
        if(!parsePathVariablesOfAnalysisCrack(album_id, analysis_id, analysis_crack_id) || !isValidTypeName(analysis_type_name))
            return ResponseEntity.badRequest().build();

        AnalysisCrackModel updatedModel = analysisCrackService.updateEntity(an_c_id, model);
//        log.info("model: " + updatedModel);
        ResponseDTO<AnalysisCrackModel> response = ResponseDTO.<AnalysisCrackModel>builder()
                .uri(getUri(uri_analysis_crack))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a analysisCrack
    @DeleteMapping(uri_analysis_crack)
    public ResponseEntity<?> deleteAnalysisCrack(
            @PathVariable String album_id,
            @PathVariable String analysis_id,
            @PathVariable String analysis_type_name,
            @PathVariable String analysis_crack_id) {

        if(!parsePathVariablesOfAnalysisCrack(album_id, analysis_id, analysis_crack_id) || !isValidTypeName(analysis_type_name))
            return ResponseEntity.badRequest().build();

        boolean deleted = analysisCrackService.deleteEntity(an_c_id);
//        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_analysis_crack))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a camera
    @PutMapping(uri_analysis_crack_update)
    public ResponseEntity<?> modifyAnalysisCrack(
            @PathVariable String album_id,
            @PathVariable String analysis_id,
            @PathVariable String analysis_type_name,
            @PathVariable String analysis_crack_id,
            @RequestBody JsonModel modifiedModel)
    {
        if(!parsePathVariablesOfAnalysisCrack(album_id, analysis_id, analysis_crack_id) || !isValidTypeName(analysis_type_name))
            return ResponseEntity.badRequest().build();

        AnalysisCrackModel analysisCrackModel = analysisCrackService.getEntityById(a_id, an_c_id);
        if (analysisCrackModel == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean result = analysisCrackService.modifyAnalysisCrack(modifiedModel, analysisCrackModel);
//        boolean result = workerService.calDistanceSingle(a_id, updatedModel, model);

        analysisCrackModel.setCrackCount(modifiedModel.getInfo().size());
        analysisCrackService.updateEntity(analysisCrackModel.getId(), analysisCrackModel);

        log.info("success : " + result);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_analysis_crack))
                .success(result)
                .result(result)
                .build();

        return ResponseEntity.ok().body(response);
    }

//    @PutMapping(uri_analysis_crack_update)
//    public ResponseEntity<?> updateAnalysisCrackJsonFile(
//            @PathVariable String album_id,
//            @PathVariable String analysis_id,
//            @PathVariable String analysis_type_name,
//            @PathVariable String analysis_crack_id,
//            @RequestBody ArrayList<JsonInfoModel> updatedModel)
//    {
//        if(!parsePathVariablesOfAnalysisCrack(album_id, analysis_id, analysis_crack_id) || !isValidTypeName(analysis_type_name))
//            return ResponseEntity.badRequest().build();
//
//        AnalysisCrackModel model = analysisCrackService.getEntityById(a_id, an_c_id);
//        if (model == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        String base = "/hdd_ext/part5/SIRIUS_REPOSITORY/LEMPStack/app";
//        String crackPath = Paths.get(base, model.getCracksInfoPath().substring(model.getCracksInfoPath().indexOf("/repo"))).toString();
//        AlbumPhotoModel albumPhotoModel = albumPhotoService.getEntityById(model.getPhotoId());
//        saveFile(crackPath, updatedModel);
//
//        AlbumModel albumModel = albumService.getEntityById(a_id);
//        boolean isSuccess = false;
//        try {
//
//            String runPath = "/home/sb/workspace/calc_dis_ver_2/build_single/calcDistance";
//            String imgPath = Paths.get(base,albumPhotoModel.getAlbumPhotoPath().substring(albumPhotoModel.getAlbumPhotoPath().indexOf("/repo"))).toString();
//            String pcdPath = Paths.get(base,"/repo/facility/",
//                    String.valueOf(albumModel.getFacilityId()), "maps", String.valueOf(albumModel.getFacilityMapId()), "GlobalMap.pcd").toString();
//            String[] calArguments = {imgPath,pcdPath};
//            ExecuteScript.executeShellScript(null, null, runPath, calArguments);
//            isSuccess = true;
//        } catch (IOException | InterruptedException e) {
//            log.error("Calcdistance update json error : "+e);
//        }
//
//        model.setCrackCount(updatedModel.size());
//        analysisCrackService.updateEntity(model.getId(), model);
//
//        log.info("success : " + isSuccess);
//        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
//                .uri(getUri(uri_analysis_crack))
//                .success(isSuccess)
//                .result(isSuccess)
//                .build();
//
//        return ResponseEntity.ok().body(response);
//    }


}
