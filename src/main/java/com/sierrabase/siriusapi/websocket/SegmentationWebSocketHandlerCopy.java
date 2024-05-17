package com.sierrabase.siriusapi.websocket;


import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.service.album.AlbumService;
import com.sierrabase.siriusapi.service.analysis.AnalysisCrackService;
import com.sierrabase.siriusapi.service.analysis.AnalysisService;
import com.sierrabase.siriusapi.service.worker.WorkerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


@Slf4j
@AllArgsConstructor
@Component("segmentationWebSocketHandlerrrrr")
public class SegmentationWebSocketHandlerCopy extends AbstractWebSocketHandler {
    private AnalysisService analysisService;
    private AlbumService albumService;
    private AnalysisCrackService analysisCrackService;
    private WorkerService workerService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // client로부터 메세지 받음
        String payload = message.getPayload();
        JSONObject jsonObject = validateMessageForAlbumId(payload, session);
        if (jsonObject == null) return;

        // 넘겨받은 albumId에 데이터가 들어있는지 확인
        AnalysisModel analysisModel = analysisService.getEntityById(jsonObject.getInt("analysisId"));
        AlbumModel albumModel = albumService.getEntityById(analysisModel.getAlbumId());

        if (analysisModel == null) {
            session.sendMessage(new TextMessage("[Error] : Data not found!"));
            return;
        }

        String directory = Paths.get("/hdd_ext/part5/SIRIUS_REPOSITORY/LEMPStack/app/repo/album/",
                String.valueOf(analysisModel.getAlbumId())).toString();
        String nignxURI = "http://211.224.129.230:61000";

        try {
            log.info("분석 시작");
//            boolean inferenceResult = workerService.inferenceByMultiGpu(directory);


//            String pythonPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/venv_seg/bin/torchrun";
//            String gpuNum = "--nproc_per_node=6";
//            String scriptPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/inferences/inference_and_quantification_mmseg_torchrun.py";

            // single start
            String pythonPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/venv_seg/bin/python3";
            String scriptPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/inferences/inference_and_quantification_mmseg_single_part4.py";
            // single end

            String[] aiArguments = {"--config", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/convnext_tiny_fpn_crack.py",
                    "--checkpoint", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/iter_32000.pth",
                    "--srx_dir", directory + "/origin",
                    "--srx_suffix", ".JPG"};

            long startTimeSecFirst = System.nanoTime();
//            ExecuteScript.executeShellScript(pythonPath, gpuNum, scriptPath, aiArguments);

            // single
            ExecuteScript.executeShellScript(pythonPath, null, scriptPath, aiArguments);
            // single


            log.info("python inference 실행 시간 : " + ExecuteScript.getProcessTime(startTimeSecFirst) + "초");

            long startTimeSecSecond = System.nanoTime();
            String runPath = "/home/sb/workspace/calc_dis_ver_2/build/calcDistance";
            String pcdPath = directory.substring(0, directory.indexOf("/album")) + "/facility/" + albumModel.getFacilityId() + "/maps/" + albumModel.getFacilityMapId()
                    + "/GlobalMap.pcd";
            String[] calArguments = {directory, pcdPath};
            log.info("calArgruments : " + Arrays.toString(calArguments));
            ExecuteScript.executeShellScript(null, null, runPath, calArguments);
            log.info("cal_distance 실행 시간 : " + ExecuteScript.getProcessTime(startTimeSecSecond) + "초");

            long startTimeSecThird = System.nanoTime();
            String runSecPath = "/home/sb/Desktop/vsc/0926koceti/analyzer_cracks/crack_points.py";
            String[] calSecArguments = {directory + "/analysis"};
            ExecuteScript.executeShellScript("python3", null, runSecPath, calSecArguments);
            log.info("cal_distance points 실행 시간 : " + ExecuteScript.getProcessTime(startTimeSecThird) + "초");

            File[] files = new File(directory + "/analysis").listFiles();
            Arrays.sort(files);
//            String pattern = "^\\d{8}_\\d{6}_-?\\d+\\.\\d+_\\d+\\.\\d+_\\d+\\.\\d+_(-?\\d+\\.\\d+)_(-?\\d+\\.\\d+)_(-?\\d+\\.\\d+)_\\d{5}_\\d+_\\d+\\.json$";

            for (File file : files) {
                if (!file.getName().endsWith("_seg_points.json")) { // file.getName().matches(pattern)
                    String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                    int crackCount;
                    if (content.length() != 5) {
                        JSONArray jsonArray = new JSONArray(content);
                        crackCount = jsonArray.length();
                    } else {
                        crackCount = 0;
                    }
                    AnalysisCrackModel analysisCrackModel = new AnalysisCrackModel(albumModel.getId(), analysisModel.getId(),
                            nignxURI + file.getPath().substring(file.getPath().indexOf("/repo")), crackCount);
                    analysisCrackService.createEntity(analysisCrackModel);
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("Analysis Execute Error : " + e);
        }

        // analyses db update
        analysisModel.setStatus("Completed");
        analysisService.updateEntity(analysisModel.getId(), analysisModel);
        log.info("message before !!");
        super.handleTextMessage(session, new TextMessage("[Message] Success"));
        log.info("message after !!");
    }

    private JSONObject validateMessageForAlbumId(String payload, WebSocketSession session) throws Exception {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(payload);
        } catch (JSONException e) {
            session.sendMessage(new TextMessage("[Error] : Invalid JSON format!"));
            return null;
        }

        if (!jsonObject.has("analysisId")) {
            session.sendMessage(new TextMessage("[Error] : JSON should have an 'analysisId' key!"));
            return null;
        }

        return jsonObject;
    }
}
