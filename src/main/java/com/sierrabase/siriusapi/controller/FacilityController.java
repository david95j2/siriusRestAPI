package com.sierrabase.siriusapi.controller;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.FacilityMapModel;
import com.sierrabase.siriusapi.model.FacilityModel;
import com.sierrabase.siriusapi.model.FtpCredentialModel;
import com.sierrabase.siriusapi.service.FacilityMapService;
import com.sierrabase.siriusapi.service.FacilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/facilities")
public class FacilityController {
    static private final String apiTag = "/api/facilities";

    @Autowired
    private FacilityService facilityService;
    @Value("${path.repository.base}")
    private String repository_path;
    @Autowired
    private FtpConfig ftpConfig;

    static private final String uri_facility = "/{facility_id}";
    static private final String uri_thumbnails = uri_facility + "/thumbnails";

    static private final String uri_maps = uri_facility + "/maps";
    static private final String uri_map = uri_maps + "/{facility_map_id}";

    static private final String uri_ftp_credentials = uri_map + "/ftp-credentials";
    static private final String uri_maps_upload = uri_map + "/upload";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int f_id, f_m_id;
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

    // GET - facilities
    @GetMapping
    public ResponseEntity<?> getFacilities() {
        ArrayList<FacilityModel> facilityList = facilityService.getAllEntities();
//        log.info("model: " + facilityList);
        ResponseDTO<ArrayList<FacilityModel>> response = ResponseDTO.<ArrayList<FacilityModel>>builder()
                .uri(getUri(""))
                .success(facilityList != null)
                .result(facilityList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a facility
    @GetMapping(uri_facility)
    public ResponseEntity<?> getFacilityByFacilityId(
            @PathVariable(required=true)String facility_id)
    {
        if(!parsePathVariablesOfFacility(facility_id))
            return ResponseEntity.badRequest().build();

        FacilityModel facility = facilityService.getEntityById(f_id);
//        log.info("model: " + facility);
        ResponseDTO<FacilityModel> response = ResponseDTO.<FacilityModel>builder()
                .uri(getUri(uri_facility))
                .success(facility != null)
                .result(facility)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a new facility
    @PostMapping
    public ResponseEntity<?> createFacility(
            @RequestBody FacilityModel facilityModel) {
        FacilityModel createdFacility = facilityService.createEntity(facilityModel);
//        log.info("model: " + createdFacility);
        ResponseDTO<FacilityModel> response = ResponseDTO.<FacilityModel>builder()
                .uri(getUri(""))
                .success(createdFacility != null)
                .result(createdFacility)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a facility
    @PutMapping(uri_facility)
    public ResponseEntity<?> updateFacility(
            @PathVariable(required=true)String facility_id,
            @RequestBody FacilityModel facilityModel) {

        if(!parsePathVariablesOfFacility(facility_id))
            return ResponseEntity.badRequest().build();
        FacilityModel updatedFacility = facilityService.updateEntity(f_id, facilityModel);
//        log.info("model: " + updatedFacility);
        ResponseDTO<FacilityModel> response = ResponseDTO.<FacilityModel>builder()
                .uri(getUri(uri_facility))
                .success(updatedFacility != null)
                .result(updatedFacility)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a facility
    @DeleteMapping(uri_facility)
    public ResponseEntity<?> deleteFacility(@PathVariable(required=true)String facility_id) {
        if(!parsePathVariablesOfFacility(facility_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = facilityService.deleteEntity(f_id);
//        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_facility))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    @Autowired
    private FacilityMapService facilityMapService;

    // GET - maps of a facility
    @GetMapping(uri_maps)
    public ResponseEntity<?> getMaps(
            @PathVariable(required=true)String facility_id)
    {
        if(!parsePathVariablesOfFacility(facility_id))
            return ResponseEntity.badRequest().build();

        ArrayList<FacilityMapModel> facilityMapList = facilityMapService.getEntitiesByFacilityId(f_id);
//        log.info("model: " + facilityMapList);
        ResponseDTO<ArrayList<FacilityMapModel>> response = ResponseDTO.<ArrayList<FacilityMapModel>>builder()
                .uri(getUri(uri_maps))
                .success(facilityMapList != null)
                .result(facilityMapList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a map of a facility
    @GetMapping(uri_map)
    public ResponseEntity<?> getMap(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id)
    {
        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        FacilityMapModel facilityMap = facilityMapService.getEntityById(f_m_id);
//        log.info("model: " + facilityMap);
        ResponseDTO<FacilityMapModel> response = ResponseDTO.<FacilityMapModel>builder()
                .uri(getUri(uri_map))
                .success(facilityMap != null)
                .result(facilityMap)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a new map of a facility
    @PostMapping(uri_maps)
    public ResponseEntity<?> createMap(
            @PathVariable(required=true)String facility_id,
            @RequestBody FacilityMapModel facilityMapModel)
    {

        if(!parsePathVariablesOfFacility(facility_id))
            return ResponseEntity.badRequest().build();

        FacilityMapModel createdFacilityMap = facilityMapService.createEntity(facilityMapModel);
//        log.info("model: " + createdFacilityMap);
        ResponseDTO<FacilityMapModel> response = ResponseDTO.<FacilityMapModel>builder()
                .uri(getUri(uri_maps))
                .success(createdFacilityMap != null)
                .result(createdFacilityMap)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // PUT - a map of a facility
    @PutMapping(uri_map)
    public ResponseEntity<?> updateMap(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id,
            @RequestBody FacilityMapModel facilityMapModel)
    {
        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        FacilityMapModel updateFacilityMap = facilityMapService.updateEntity(f_m_id, facilityMapModel);
//        log.info("model: " + updateFacilityMap);
        ResponseDTO<FacilityMapModel> response = ResponseDTO.<FacilityMapModel>builder()
                .uri(getUri(uri_map))
                .success(updateFacilityMap != null)
                .result(updateFacilityMap)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a map of a facility
    @DeleteMapping(uri_map)
    public ResponseEntity<?> deleteMap(
            @PathVariable(required=true)String facility_id,
            @PathVariable(required=true)String facility_map_id)
    {
        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = facilityMapService.deleteEntity(f_m_id);
//        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_map))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a new map of a facility
    @PostMapping(uri_thumbnails)
    public ResponseEntity<?> uploadThumbnail(
            @PathVariable(required=true)String facility_id,
            @RequestParam("file") MultipartFile file)
    {

        if(!parsePathVariablesOfFacility(facility_id))
            return ResponseEntity.badRequest().build();

        if (file.isEmpty())
            return ResponseEntity.badRequest().build();

        FacilityModel facility = facilityService.getEntityById(f_id);
        Boolean success = facilityMapService.uploadFile(file,f_id);
        if (!success) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }


        String nginxURI = ftpConfig.getNginxUri();
        String newUrl = URICreator.pathToString(nginxURI ,"facility", String.valueOf(f_id),"thumbnails", file.getOriginalFilename().replaceAll(" ",""));
        facility.setThumbnailUrl(newUrl);
        facilityService.updateEntity(f_id, facility);

//        log.info("success : " + success);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_maps))
                .success(success)
                .result(success)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a map of a facility
    @GetMapping(uri_ftp_credentials)
    public ResponseEntity<?> getFtpCredentials(
            @PathVariable String facility_id,
            @PathVariable String facility_map_id)
    {
        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        FacilityMapModel facilityMap = facilityMapService.getEntityById(f_m_id);
        String relativePath;
        if (facilityMap.getUrl().isEmpty()) {
            relativePath = URICreator.pathToString("facility",String.valueOf(f_id),"maps",String.valueOf(f_m_id));
        } else {
            relativePath = facilityMap.getUrl().substring(
                    facilityMap.getUrl().indexOf("/repo") + 6,
                    facilityMap.getUrl().lastIndexOf("/"));
        }
        FtpCredentialModel model = new FtpCredentialModel(ftpConfig, relativePath);

        ResponseDTO<FtpCredentialModel> response = ResponseDTO.<FtpCredentialModel>builder()
                .uri(getUri(uri_ftp_credentials))
                .success(true)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }


    // 맵 올렸다는 완료메세지를 받으면 db에 map 추가
    @PostMapping(uri_maps_upload)
    public ResponseEntity<?> uploadMaps(
            @PathVariable String facility_id,
            @PathVariable String facility_map_id)
    {
        if(!parsePathVariablesOfFacilityMap(facility_id, facility_map_id))
            return ResponseEntity.badRequest().build();

        String targetPath =URICreator.pathToString(repository_path,"facility",String.valueOf(f_id),
                "maps",String.valueOf(f_m_id),"GlobalMap.pcd");
        File targetFile = new File(targetPath);

        if (!targetFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        String newUrl = URICreator.pathToString(ftpConfig.getNginxUri(),targetFile.getPath().substring(targetFile.getPath().indexOf("/repo")));

        FacilityMapModel uploadModel = new FacilityMapModel();
        uploadModel.setFacilityId(f_id);
        uploadModel.setName(targetFile.getName());
        uploadModel.setUrl(newUrl);
        uploadModel.setCreatedDatetime(ZonedDateTime.now(ZoneId.of("UTC")));

        FacilityMapModel model = facilityMapService.updateEntity(f_m_id, uploadModel);

        ResponseDTO<FacilityMapModel> response = ResponseDTO.<FacilityMapModel>builder()
                .uri(getUri(uri_maps_upload))
                .success(true)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
