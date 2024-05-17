package com.sierrabase.siriusapi.websocket;



import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.service.album.AlbumService;
import com.sierrabase.siriusapi.service.analysis.AnalysisCrackService;
import com.sierrabase.siriusapi.service.analysis.AnalysisCrackWorker;
import com.sierrabase.siriusapi.service.analysis.AnalysisService;
import com.sierrabase.siriusapi.service.worker.WorkerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Component("segmentationWebSocketHandler")
public class SegmentationWebSocketHandler extends AbstractWebSocketHandler {
    private AlbumService albumService;
    private AnalysisService analysisService;
    private AnalysisCrackService analysisCrackService;
    private AnalysisCrackWorker analysisCrackWorker;
    private FtpConfig ftpConfig;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // client로부터 메세지 받음
        String clinetMessage = message.getPayload();
        JSONObject analysisKey = validateMessageForAlbumId(clinetMessage, session);
        if (analysisKey == null) return;

        // 넘겨받은 albumId에 데이터가 들어있는지 확인
        AnalysisModel analysisModel = analysisService.getEntityById(analysisKey.getInt("analysisId"));
        if (analysisModel == null) {
            session.sendMessage(new TextMessage("[Error] : Data not found!"));
            return;
        }

        // initialize variable
        AlbumModel albumModel = albumService.getEntityById(analysisModel.getAlbumId());
        Integer albumId = albumModel.getId();


        // Inference
        log.info("분석 시작");
        long startTimeFirst = System.nanoTime();
//        boolean inferenceResult = analysisCrackWorker.inferenceByMultiGpu(albumId); // multi
        boolean inferenceResult = analysisCrackWorker.inferenceBySingleGpu(albumId); // single
        if (!inferenceResult) {
            super.handleTextMessage(session, new TextMessage("[Message Error] Inference Failed"));
            return;
        }
        log.info("python inference 실행 시간 : "+ExecuteScript.getProcessTime(startTimeFirst) + "초");


        // Calculation of each crack distance
        long startTimeSecond = System.nanoTime();
        boolean calculateResult = analysisCrackWorker.computeCrackToCameraDistancesForFolder(albumModel);
        if (!calculateResult) {
            super.handleTextMessage(session, new TextMessage("[Message Error] CalDistance Failed"));
            return;
        }
        log.info("cal_distance 실행 시간 : "+ExecuteScript.getProcessTime(startTimeSecond)+ "초");


        // Reset Index of crack
        long startTimeThird = System.nanoTime();
        boolean crackPointResult = analysisCrackWorker.resetIndex(albumId);
        if (!crackPointResult) {
            super.handleTextMessage(session, new TextMessage("[Message Error] resetIndex Failed"));
            return;
        }
        log.info("cal_distance points 실행 시간 : "+ExecuteScript.getProcessTime(startTimeThird)+ "초");


        // Create Entity
        String sorucePath = URICreator.pathToString(analysisCrackWorker.getRepositoryPath(),"album",String.valueOf(albumId),"analysis");
        File[] files = new File(sorucePath).listFiles();
        Arrays.sort(files);
//            String pattern = "^\\d{8}_\\d{6}_-?\\d+\\.\\d+_\\d+\\.\\d+_\\d+\\.\\d+_(-?\\d+\\.\\d+)_(-?\\d+\\.\\d+)_(-?\\d+\\.\\d+)_\\d{5}_\\d+_\\d+\\.json$";

        for (File file : files) {
            if (!file.getName().endsWith("_seg_points.json")) { // file.getName().matches(pattern)
                String jsonContent = new String(Files.readAllBytes(Paths.get(file.getPath())));
                int crackCount;
                if (jsonContent.length() != 5) {
                    crackCount = new JSONArray(jsonContent).length();
                } else {
                    crackCount = 0;
                }

                AnalysisCrackModel analysisCrackModel = new AnalysisCrackModel(albumModel.getId(), analysisModel.getId(),
                        ftpConfig.getNginxUri()+file.getPath().substring(file.getPath().indexOf("/album")),crackCount);
                analysisCrackService.createEntity(analysisCrackModel);
            }
        }

        // analyses db update
        analysisModel.setStatus("Completed");
        analysisService.updateEntity(analysisModel.getId(), analysisModel);
        super.handleTextMessage(session, new TextMessage("[Message] Success"));

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
