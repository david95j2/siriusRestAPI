package com.sierrabase.siriusapi.controller.analysis;

import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.service.album.ModelingService;
import com.sierrabase.siriusapi.service.worker.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class Analysis3DModelController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_modeling = uri_album + "/modeling";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }


    private int a_id;

    private boolean parsePathVariablesOfAlbum(String albumId) {
        a_id = URIParser.parseStringToIntegerId(albumId);
        if (a_id < 0)
            return false;
        return true;
    }

    @Value("${path.repository.base}")
    private String repository_path;

    @Autowired
    private WorkerService workerService;
    @Autowired
    private ModelingService modelingService;

    @PostMapping(uri_modeling)
    public ResponseEntity<?> webhookListener(
            @PathVariable String album_id,
            @RequestBody SourceInfoModel model) {

        if (!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }

        boolean result = modelingService.importModel(a_id,model);



        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_modeling))
//                .success(modelList != null)
//                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }
}
