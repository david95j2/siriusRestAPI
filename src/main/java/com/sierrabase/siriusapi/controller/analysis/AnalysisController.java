
package com.sierrabase.siriusapi.controller.analysis;



import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.service.album.AlbumService;
import com.sierrabase.siriusapi.service.analysis.AnalysisCrackService;
import com.sierrabase.siriusapi.service.analysis.AnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/api/albums")
public class AnalysisController {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";
    static private final String uri_analyses = uri_album + "/analyses";
    static private final String uri_analysis = uri_analyses + "/{analysis_id}";

    private String getUri(final String methodName) {
        return apiTag + methodName;
    }

    private int a_id, an_id;
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

    @Autowired
    private AnalysisService analysisService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AnalysisCrackService analysisCrackService;

    // GET - analyses
    @GetMapping(uri_analyses)
    public ResponseEntity<?> getAnalyses(@PathVariable String album_id) {

        if(!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }

        ArrayList<AnalysisModel> modelList = analysisService.getAllEnitities(a_id);
        log.info("model: " + modelList);
        ResponseDTO<ArrayList<AnalysisModel>> response = ResponseDTO.<ArrayList<AnalysisModel>>builder()
                .uri(getUri(uri_analyses))
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - a analysis
    @GetMapping(uri_analysis)
    public ResponseEntity<?> getAnalysis(@PathVariable String album_id,
                                           @PathVariable String analysis_id) {

        if(!parsePathVariablesOfAnalysis(album_id,analysis_id)) {
            ResponseEntity.badRequest().build();
        }

        AnalysisModel model = analysisService.getEntityByCondition(an_id, a_id);

        log.info("model: " + model);
        ResponseDTO<AnalysisModel> response = ResponseDTO.<AnalysisModel>builder()
                .uri(getUri(uri_analysis))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - a analysis
    @PostMapping(uri_analyses)
    public ResponseEntity<?> createAnalysis(
            @PathVariable String album_id,
            @RequestBody AnalysisModel model) {

        if(!parsePathVariablesOfAlbum(album_id))
            return ResponseEntity.badRequest().build();

        AlbumModel albumModel = albumService.getEntityById(a_id);
        if (albumModel == null)
            return ResponseEntity.badRequest().build();

//        File directory = new File(Paths.get("/hdd_ext/part5/SIRIUS_REPOSITORY/LEMPStack/app/repo/album/",
//                String.valueOf(a_id)).toString());
//        String nignxURI = "http://211.224.129.230:61000";

        AnalysisModel createdModel = analysisService.createEntity(model);;

//        try {
//            log.info("분석 시작");
////            String pythonPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/venv_seg/bin/python3";
////            String scriptPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/inferences/inference_and_quantification_mmseg_single.py";
//            String pythonPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/venv_seg/bin/torchrun";
//            String gpuNum = "--nproc_per_node=6";
//            String scriptPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/inferences/inference_and_quantification_mmseg.py";
//            String[] aiArguments = {"--config", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/convnext_tiny_fpn_crack.py",
//                    "--checkpoint", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/iter_32000.pth",
//                    "--srx_dir", directory.getPath()+"/origin",
//                    "--srx_suffix", ".JPG"};
//
//            long startTimeSecFirst = System.nanoTime();
//            ExecuteScript.executeShellScript(pythonPath, gpuNum, scriptPath, aiArguments);
//            log.info("python inference 실행 시간 : "+ExecuteScript.getProcessTime(startTimeSecFirst) + "초");
//
//            long startTimeSecSecond = System.nanoTime();
//            String runPath = "/home/sb/workspace/calc_dis/build/calcDistance";
//            String pcdPath = directory.getPath().substring(0, directory.getPath().indexOf("/album"))+"/facility/"+albumModel.getFacilityMapId()
//                    +"/GlobalMap.pcd";
//            String[] calArguments = {"0", directory.getPath()+"/origin",pcdPath};
//            ExecuteScript.executeShellScript(null, null, runPath, calArguments);
//            log.info("cal_distance 실행 시간 : "+ExecuteScript.getProcessTime(startTimeSecSecond)+ "초");
//
//            createdModel = analysisService.createEntity(model);
//
//            File[] files = new File(directory+"/analysis").listFiles();
//            Arrays.sort(files);
//            String pattern = "(\\w+_){10}\\.json$";
//            Pattern r = Pattern.compile(pattern);
//
//            for (File file : files) {
//                if (r.matcher(file.getName()).find()) {
//                    AnalysisCrackModel analysisCrackModel = new AnalysisCrackModel(a_id, createdModel.getId(),
//                            nignxURI+file.getPath().substring(file.getPath().indexOf("/repo")));
//                    analysisCrackService.createEntity(analysisCrackModel);
//                } else {
//                    log.info("버려짐 : "+file.getName());
//                }
//            }
//        } catch (IOException | InterruptedException e) {
//            log.error("Analysis Execute Error : "+e);
//            return ResponseEntity.internalServerError().body("Error processing analysis");
//        }



        log.info("model: " + createdModel);
        ResponseDTO<AnalysisModel> response = ResponseDTO.<AnalysisModel>builder()
                .uri(getUri(uri_analyses))
                .success(createdModel != null)
                .result(createdModel)
                .build();
        return ResponseEntity.ok().body(response);
    }

    // PUT - a analysis
    @PutMapping(uri_analysis)
    public ResponseEntity<?> updateAnalysis(
            @PathVariable String album_id,
            @PathVariable String analysis_id,
            @RequestBody AnalysisModel model)
    {
        if(!parsePathVariablesOfAnalysis(album_id,analysis_id))
            return ResponseEntity.badRequest().build();

        AlbumModel albumModel = albumService.getEntityById(a_id);
        if (albumModel == null)
            return ResponseEntity.badRequest().build();

        AnalysisModel updatedModel = analysisService.updateEntity(an_id, model);
        log.info("model: " + updatedModel);
        ResponseDTO<AnalysisModel> response = ResponseDTO.<AnalysisModel>builder()
                .uri(getUri(uri_analysis))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a albumPhoto
    @DeleteMapping(uri_analysis)
    public ResponseEntity<?> deleteAlbumPhoto(
            @PathVariable String album_id,
            @PathVariable String analysis_id)
    {
        if(!parsePathVariablesOfAnalysis(album_id, analysis_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = analysisService.deleteEntity(an_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_analysis))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }

}
