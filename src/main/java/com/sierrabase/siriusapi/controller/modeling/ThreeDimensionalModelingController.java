package com.sierrabase.siriusapi.controller.modeling;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.dto.ResponseCodeDTO;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.FacilityModel;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalFacilityModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModelingModel;
import com.sierrabase.siriusapi.service.album.ModelingService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalFacilityService;
import com.sierrabase.siriusapi.service.modeling.ThreeDimensionalModelingService;
import com.sierrabase.siriusapi.service.worker.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class ThreeDimensionalModelingController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_modelings = uri_album + "/modeling";
    static private final String uri_modeling = uri_modelings + "/{modeling_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    @Autowired
    private FtpConfig ftpConfig;
    private int a_id, m_id;

    private boolean parsePathVariablesOfAlbum(String albumId) {
        a_id = URIParser.parseStringToIntegerId(albumId);
        if (a_id < 0)
            return false;
        return true;
    }

    private boolean parsePathVariablesOfModeling(String albumId, String modelingId) {
        if(!parsePathVariablesOfAlbum(albumId))
            return false;
        m_id = URIParser.parseStringToIntegerId(modelingId);
        if(m_id < 0)
            return false;
        return true;
    }


    @Autowired
    private ThreeDimensionalModelingService threeDimensionalModelingService;
    @Autowired
    private ThreeDimensionalFacilityService threeDimensionalFacilityService;
    // GET - a facility
    @GetMapping(uri_modelings)
    public ResponseEntity<?> getModelings(
            @PathVariable String album_id)
    {
        if(!parsePathVariablesOfAlbum(album_id))
            return ResponseEntity.badRequest().build();

        ArrayList<ThreeDimensionalModelingModel> modelList = threeDimensionalModelingService.getAllEntities(a_id);
//        log.info("model: " + facility);
        ResponseDTO<ArrayList<ThreeDimensionalModelingModel>> response = ResponseDTO.<ArrayList<ThreeDimensionalModelingModel> >builder()
                .uri(getUri(uri_modeling))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping(uri_modeling)
    public ResponseEntity<?> getModelingById(
            @PathVariable String album_id,
            @PathVariable String modeling_id)
    {
        if(!parsePathVariablesOfModeling(album_id,modeling_id))
            return ResponseEntity.badRequest().build();

        ThreeDimensionalModelingModel model = threeDimensionalModelingService.getEntityById(m_id);
//        log.info("model: " + facility);
        ResponseDTO<ThreeDimensionalModelingModel> response = ResponseDTO.<ThreeDimensionalModelingModel>builder()
                .uri(getUri(uri_modeling))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(uri_modelings)
    public ResponseEntity<?> webhookListener(
            @PathVariable String album_id,
            @RequestBody SourceInfoModel model) {

        if (!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }

        boolean result = threeDimensionalModelingService.importModel(a_id,model);

        ThreeDimensionalModelingModel modelingModel = new ThreeDimensionalModelingModel();
        modelingModel.setAlbumId(a_id);
        modelingModel.setName("import model");
        modelingModel.setType(1);
        modelingModel.setTypeName("glb");
        modelingModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
        if (!result) {
            modelingModel.setStatus("Server Error");
            threeDimensionalModelingService.createEntity(modelingModel);
            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Can not import 3D model"));
        }
        modelingModel.setStatus("Completed");
        ThreeDimensionalModelingModel createdModelingModel = threeDimensionalModelingService.createEntity(modelingModel);
        if (createdModelingModel == null) {
            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Can not create modeling"));
        }

        ThreeDimensionalFacilityModel facilityModel = new ThreeDimensionalFacilityModel();
        facilityModel.setThreeDimensionalModelingId(createdModelingModel.getId());
        facilityModel.setAlbumId(a_id);
        facilityModel.setType(1);
        facilityModel.setTypeName("down sampled");
        System.out.println(ftpConfig.getNginxUri()+"/"+URICreator.pathToString("album",String.valueOf(a_id),"3D_modeling/downsampled_model.glb"));
        facilityModel.setThreeDimensionalFacilityUrl(ftpConfig.getNginxUri()+"/"+URICreator.pathToString("album",String.valueOf(a_id),"3D_modeling/downsampled_model.glb"));
        facilityModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));
        ThreeDimensionalFacilityModel createdFacilityModel = threeDimensionalFacilityService.createEntity(facilityModel);
        log.info("create 3d facility : "+createdFacilityModel);
        if (createdFacilityModel == null) {
            return ResponseEntity.ok().body(new ResponseCodeDTO<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Can not create model"));
        }

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_modelings))
//                .success(modelList != null)
//                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
