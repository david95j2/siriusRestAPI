package com.sierrabase.siriusapi.service.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.config.FtpConfig;
import com.sierrabase.siriusapi.model.SourceInfoModel;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.JsonModel;
import com.sierrabase.siriusapi.model.inspection.FitAreaProgramModel;
import com.sierrabase.siriusapi.service.album.AlbumPhotoPosService;
import com.sierrabase.siriusapi.service.album.AlbumPhotoService;
import com.sierrabase.siriusapi.service.album.AlbumService;
import com.sierrabase.siriusapi.service.album.CameraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@Service
public class WorkerServiceCopy {

    @Value("${path.repository.base}")
    private String repository_path;

    @Value("${path.worker.base}")
    private String worker_path;

    @Autowired
    private FtpConfig ftpConfig;
    @Autowired
    CameraService cameraService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumPhotoService albumPhotoService;
    @Autowired
    private AlbumPhotoPosService albumPhotoPosService;

    public Boolean startFitArea(int f_id, int f_m_id, FitAreaProgramModel model) {
        String directory = Paths.get(repository_path,"facility", String.valueOf(f_id), "maps", String.valueOf(f_m_id), "GlobalMap.pcd").toString();
        String workerProgram = Paths.get(worker_path,"pcd-manager3/build/fit_area").toString();
        String[] argument = {directory, model.getPort(), model.getUserId()};

        try {
            int resultCode = ExecuteScript.executeShellScript(null, null, workerProgram, argument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("fit_area error : "+e);
            return false;
        }
        return true;
    }

    public Boolean ftpDownload(int a_id, String localPath, SourceInfoModel model, Boolean extract) {
        String ftpRunPath = Paths.get(worker_path,"ftpClient.py").toString();
        String remotePath = model.getUrl();
//        String[] ftpArgument = {model.getFtpIp(), model.getFtpPort(), model.getFtpId(), model.getFtpPassword(), "download", localPath, remotePath};

//        try {
//            int resultCode = ExecuteScript.executeShellScript("python3", null, ftpRunPath, ftpArgument);
//            if (resultCode != 0) return false;
//        } catch (IOException | InterruptedException e) {
//            log.error("FTP Album Download Error : " + e);
//            return false;
//        }
//        if (extract) {
//            return extractFile(a_id, localPath, remotePath, model);
//        }
        return true;
    }

    // POST - extract compressed file
    private boolean extractFile(Integer albumId , String localPath, String remotePath, SourceInfoModel model) {
        File directory = new File(localPath);
        String nignxURI = ftpConfig.getNginxUri();
        String zipFile = Paths.get(repository_path,remotePath).toString();
        String[] extractArgument = {zipFile, directory.getPath()};
        log.warn("EXTRACT : "+model);
        try {
            // 압축 해제 쉘 스크립트 실행
            log.info("extract start ! (albumId : " + albumId + ")");
            ExecuteScript.executeShellScript(null, null, "sb-extract", extractArgument);
            log.info("extract end ! (albumId : " + albumId + ")");

            // 사진 폴더 검사
            File[] checkFiles = new File(directory + "/origin").listFiles();
//            if (checkFiles[0].isDirectory()) {
//                String checkDir = checkFiles[0].getPath();
//                File[] subCheckList = new File(checkDir).listFiles();
//                for (File sub : subCheckList) {
//                    if (sub.getName().equals("info.csv")) {
//                        sub.delete();
//                        continue;
//                    }
//                    Files.move(Paths.get(sub.getPath()),Paths.get(directory+"/origin/"+sub.getName()));
//                }
//                checkFiles[0].delete();
//            }
//
//            // 이미지 폴더 순회 후 DB 업데이트 (camera / photo / photo pos)
//            File[] files = new File(directory + "/origin").listFiles();
//            boolean isFirst = true;
//            CameraModel createdCameraModel = null;
//
//            for (File file : files) {
//                if (isFirst) {
//                    Map<String, String> metadataMap = albumService.extractMetadataFromImage(file);
//                    CameraModel cameraModel = new CameraModel(metadataMap);
//                    createdCameraModel = cameraService.createEntity(cameraModel);
//                    AlbumModel albumModel = albumService.getEntityById(albumId);
//                    albumModel.setCameraId(createdCameraModel.getId());
//                    albumModel.setPhotoCount(files.length);
//                    albumService.updateEntity(albumId, albumModel);
//                    isFirst = false;
//                }
//                if (file.getName().equals("info.csv")) continue;
//
//                AlbumPhotoModel albumPhotoModel = albumPhotoService.createEntity(new AlbumPhotoModel(albumId, createdCameraModel.getId(),
//                        nignxURI + file.getPath().substring(file.getPath().indexOf("/album"))));
//                albumPhotoPosService.createEntity(new AlbumPhotoPosModel(albumPhotoModel.getId(),
//                        Paths.get(albumPhotoModel.getAlbumPhotoPath()).getFileName().toString(),
//                        albumPhotoModel));
//            }
//
//            // 이미지 리사이징 쉘 스크립트 실행
//            ExecutorService executorService = Executors.newFixedThreadPool(2);
//            executorService.execute(() -> {
//                boolean thumbnailResult = resizeThumbnails(directory);
//                if (thumbnailResult) {
//                    File[] thumbnailsFiles = new File(directory + "/thumbnails").listFiles();
//                    for (File file : thumbnailsFiles) {
//                        String thumbnailsPattern = file.getPath().substring(file.getPath().indexOf("/album")).replace("thumbnails", "origin");
//                        AlbumPhotoModel resizedPhotoModel = albumPhotoService.getEntityByName(thumbnailsPattern);
//
//                        String new_path = nignxURI + file.getPath().substring(file.getPath().indexOf("/album"));
//                        resizedPhotoModel.setAlbumPhotoThumbnailsPath(new_path);
//                        new_path = new_path.replace("thumbnails", "resized");
//                        resizedPhotoModel.setAlbumPhotoResizedPath(new_path);
//                        albumPhotoService.updateEntity(resizedPhotoModel.getId(), resizedPhotoModel);
//                    }
//                } else {
//                    log.error("resize thumbnails error!");
//                }
//            });
//
//            executorService.execute(() -> {
//                boolean resizedImageResult = resizedImage(directory);
//                if (!resizedImageResult) {
//                    log.error("resize Image error!");
//                }
//            });
//            executorService.shutdown(); // 스레드 풀 종료 시작

        } catch (IOException | InterruptedException e) {
            log.error("extract process error !");
            return false;
        }
//        catch (ImageProcessingException e) {
//            throw new RuntimeException(e);
//        }

        return true;
    }

    private boolean resizeThumbnails(File directory) {
        try {
            String[] thumbnailsArgument = {directory + "/origin", directory + "/thumbnails", "300x200"};
            int resultCode = ExecuteScript.executeShellScript(null, null, "sb-img-resizer", thumbnailsArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("thumbnails resize error : "+e);
            return false;
        }
        return true;
    }

    private boolean resizedImage(File directory) {
        try {
            String[] resizeArgument = {directory + "/origin", directory + "/resized", "2100x1400"};
            int resultCode = ExecuteScript.executeShellScript(null, null, "sb-img-resizer", resizeArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("2100x1400 resize error : "+e);
            return false;
        }
        return true;
    }


    public boolean inferenceByMultiGpu(String directory) {
        String pythonPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/venv_seg/bin/torchrun";
        String gpuNum = "--nproc_per_node=6";
        String scriptPath = "/home/sb/Desktop/vsc/0926koceti/20230901_mmsegmentation/inferences/inference_and_quantification_mmseg_torchrun.py";
        String[] aiArguments = {"--config", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/convnext_tiny_fpn_crack.py",
                "--checkpoint", "/home/sb/Desktop/vsc/0926koceti/20230901_crack/iter_32000.pth",
                "--srx_dir", directory+"/origin",
                "--srx_suffix", ".JPG"};
        try {
            int resultCode = ExecuteScript.executeShellScript(pythonPath, gpuNum, scriptPath, aiArguments);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Inference error : "+e);
            return false;
        }
        return true;

    }

    public boolean calDistance(String directory, AlbumModel albumModel) {
        String runPath = Paths.get(worker_path,"calc_dis_ver_2/build/calcDistance").toString();

        String pcdPath = Paths.get(repository_path,"facility",String.valueOf(albumModel.getFacilityId()),
                "maps",String.valueOf(albumModel.getFacilityMapId()),"GlobalMap.pcd").toString();

        String[] calArguments = {directory,pcdPath};

        try {
            int resultCode = ExecuteScript.executeShellScript(null, null, runPath, calArguments);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("CalDistance error : "+e);
            return false;
        }
        return true;
    }

    public boolean crackPoints(String directory) {
        String runSecPath = Paths.get(worker_path,"modify_crack_points.py").toString();
        String[] calSecArguments = {directory+"/analysis"};

        try {
            int resultCode = ExecuteScript.executeShellScript("python3", null, runSecPath, calSecArguments);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Crack Points error : "+e);
            return false;
        }
        return true;
    }

    public boolean createGltf(String localPath) {
        String gltfRunPath = "/home/sb/workspace/programs/convert2glb.py";
        String[] gltfArgument = {"--",localPath+"/Data","Tile",localPath,"16"};

        try {
            int resultCode = ExecuteScript.executeShellScript("blender", "-b -P", gltfRunPath, gltfArgument);
            if (resultCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Blender error : "+e);
            return false;
        }
        return true;
    }

    public boolean calDistanceSingle(Integer a_id, JsonModel updatedModel, AnalysisCrackModel model) {

        String crackPath = Paths.get(repository_path, model.getCracksInfoPath().substring(model.getCracksInfoPath().indexOf("/album"))).toString();
        AlbumPhotoModel albumPhotoModel = albumPhotoService.getEntityById(model.getPhotoId());
        boolean saveResult = saveFile(crackPath, updatedModel);

        if (!saveResult) {
            log.error("Json Save Error on Update");
            return false;
        }

        AlbumModel albumModel = albumService.getEntityById(a_id);

        String runPath = Paths.get(worker_path,"calc_dis_ver_2/build_single/calcDistance").toString();
        String imgPath = Paths.get(repository_path,albumPhotoModel.getAlbumPhotoPath().substring(albumPhotoModel.getAlbumPhotoPath().indexOf("/album"))).toString();
        String pcdPath = Paths.get(repository_path,"facility", String.valueOf(albumModel.getFacilityId()), 
                "maps", String.valueOf(albumModel.getFacilityMapId()), "GlobalMap.pcd").toString();
        String[] calArguments = {imgPath,pcdPath};
        
        String jsonPath = imgPath.replace("origin","analysis").replace(".JPG",".json");
        String runPointPath = Paths.get(worker_path,"modify_crack_points.py").toString();
        String[] calPointArguments = {jsonPath};
        
        try {
            int calDistanceSingleCode = ExecuteScript.executeShellScript(null, null, runPath, calArguments);
            if (calDistanceSingleCode != 0) return false;
            int crackPointsSingleCode = ExecuteScript.executeShellScript("python3", null, runPointPath, calPointArguments);
            if (crackPointsSingleCode != 0) return false;
        } catch (IOException | InterruptedException e) {
            log.error("Calcdistance update json error : "+e);
            return false;
        }
        return true;
    }

    private static boolean saveFile(String jsonFile, JsonModel jsonModel) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("update jsonFileName : "+jsonFile);
            objectMapper.writeValue(new File(jsonFile), jsonModel.getInfo());

            String pointsFileName = jsonFile.replace(".json", "_seg_points.json");
            objectMapper.writeValue(new File(pointsFileName), jsonModel.getPoints());
            log.info("Json Update Success !");
            return true;
        } catch (IOException e) {
            log.error("Json Update Error : "+e);
            return false;
        }
    }
}
