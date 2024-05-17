package com.sierrabase.siriusapi.controller.album;


import com.drew.imaging.ImageProcessingException;
import com.sierrabase.siriusapi.common.ExecuteScript;
import com.sierrabase.siriusapi.common.URIParser;
import com.sierrabase.siriusapi.dto.ResponseDTO;
import com.sierrabase.siriusapi.model.FtpCredentialModel;
import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoPosModel;
import com.sierrabase.siriusapi.model.album.CameraModel;
import com.sierrabase.siriusapi.service.album.AlbumPhotoPosService;
import com.sierrabase.siriusapi.service.album.AlbumPhotoService;
import com.sierrabase.siriusapi.service.album.AlbumService;
import com.sierrabase.siriusapi.service.album.CameraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/albums/testtest")
public class AlbumControllerCopy {
    static private final String apiTag = "/api/albums";
    static private final String uri_album = "/{album_id}";

    static private final String uri_upload = uri_album + "/upload";
    static private final String uri_extract = uri_album + "/extract";
    static private final String uri_test = "/test";

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

    @Autowired
    CameraService cameraService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumPhotoService albumPhotoService;
    @Autowired
    private AlbumPhotoPosService albumPhotoPosService;

    // GET - albums
    @GetMapping()
    public ResponseEntity<?> getAlbums(@RequestParam Optional<Integer> facility_id,
                                       @RequestParam Optional<Integer> facility_map_id) {

        ArrayList<AlbumModel> modelList = albumService.getByConditions(facility_id, facility_map_id);
        log.info("model: " + modelList);
        ResponseDTO<ArrayList<AlbumModel>> response = ResponseDTO.<ArrayList<AlbumModel>>builder()
                .uri(apiTag)
                .success(modelList != null)
                .result(modelList)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // GET - album
    @GetMapping(uri_album)
    public ResponseEntity<?> getAlbum(@PathVariable String album_id) {

        if (!parsePathVariablesOfAlbum(album_id)) {
            ResponseEntity.badRequest().build();
        }

        AlbumModel model = albumService.getEntityById(a_id);
        log.info("model: " + model);
        ResponseDTO<AlbumModel> response = ResponseDTO.<AlbumModel>builder()
                .uri(getUri(uri_album))
                .success(model != null)
                .result(model)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - album
    @PostMapping()
    public ResponseEntity<?> postAlbum(@RequestBody AlbumModel model) {
        AlbumModel createdModel = albumService.createEntity(model);
        log.info("model: " + createdModel);
        ResponseDTO<AlbumModel> response = ResponseDTO.<AlbumModel>builder()
                .uri(getUri(uri_album))
                .success(createdModel != null)
                .result(createdModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // POST - extract compressed file
    @PostMapping(uri_extract)
    public ResponseEntity<?> unExtract(
            @PathVariable String album_id) {

        if (!parsePathVariablesOfAlbum(album_id)) {
            return ResponseEntity.badRequest().build();
        }

        File directory = new File(Paths.get("/hdd_ext/part5/SIRIUS_REPOSITORY/LEMPStack/app/repo/album",
                String.valueOf(a_id)).toString());
        String nignxURI = "http://211.224.129.230:61000";

        String compressedFilePath = exploreDirectory(directory);
        if (compressedFilePath == null) {
            return ResponseEntity.badRequest().body("Compressed file not found");
        }

        try {
            // 압축 해제 쉘 스크립트 실행
            log.info("extract start ! (albumId : " + a_id + ")");
            String[] extractArgument = {compressedFilePath, directory.getPath()};
            ExecuteScript.executeShellScript(null, null, "sb-extract", extractArgument);
            log.info("extract end ! (albumId : " + a_id + ")");
            // 리사이즈된 이미지 폴더 순회 후 DB 업데이트 (camera / photo / photo pos)
            File[] files = new File(directory + "/origin").listFiles();
            boolean isFirst = true;
            CameraModel createdCameraModel = null;

            for (File file : files) {
                if (isFirst) {
                    Map<String, String> metadataMap = albumService.extractMetadataFromImage(file);
                    CameraModel cameraModel = new CameraModel(metadataMap);
                    createdCameraModel = cameraService.createEntity(cameraModel);
                    AlbumModel albumModel = albumService.getEntityById(a_id);
                    albumModel.setCameraId(createdCameraModel.getId());
                    albumModel.setPhotoCount(files.length);
                    albumService.updateEntity(a_id, albumModel);
                    isFirst = false;
                }
                AlbumPhotoModel albumPhotoModel = albumPhotoService.createEntity(new AlbumPhotoModel(a_id, createdCameraModel.getId(),
                        nignxURI + file.getPath().substring(file.getPath().indexOf("/repo"))));
                albumPhotoPosService.createEntity(new AlbumPhotoPosModel(albumPhotoModel.getId(),
                        Paths.get(albumPhotoModel.getAlbumPhotoPath()).getFileName().toString(),
                        albumPhotoModel));
            }

            // 이미지 리사이징 쉘 스크립트 실행
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    String[] thumbnailsArgument = {directory + "/origin", directory + "/thumbnails", "300x200"};
                    ExecuteScript.executeShellScript(null, null, "sb-img-resizer", thumbnailsArgument);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                File[] thumbnailsFiles = new File(directory + "/thumbnails").listFiles();
                for (File file : thumbnailsFiles) {

                    String thumbnailsPattern = file.getPath().substring(file.getPath().indexOf("/album")).replace("thumbnails", "origin");
                    AlbumPhotoModel resizedPhotoModel = albumPhotoService.getEntityByName(thumbnailsPattern);

                    String new_path = nignxURI + file.getPath().substring(file.getPath().indexOf("/repo"));
                    resizedPhotoModel.setAlbumPhotoThumbnailsPath(new_path);
                    new_path = new_path.replace("thumbnails", "resized");
                    resizedPhotoModel.setAlbumPhotoResizedPath(new_path);
                    albumPhotoService.updateEntity(resizedPhotoModel.getId(), resizedPhotoModel);
                }

            });
            executorService.shutdown(); // 스레드 풀 종료 시작

            // 이미지 리사이징 쉘 스크립트 실행
            ExecutorService executorService_ver2 = Executors.newSingleThreadExecutor();
            executorService_ver2.execute(() -> {
                try {
                    String[] resizeArgument = {directory + "/origin", directory + "/resized", "2100x1400"};
                    ExecuteScript.executeShellScript(null, null, "sb-img-resizer", resizeArgument);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            executorService_ver2.shutdown(); // 스레드 풀 종료 시작


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error processing album");
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        }

        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(apiTag)
                .success(true)
                .result(true)
                .build();
        return ResponseEntity.ok().body(response);
    }

//    private void executeShellScript(String scriptPath, String[] arguments) throws IOException, InterruptedException {
//        List<String> command = new ArrayList<>();
//        command.add(scriptPath);
//        command.addAll(Arrays.asList(arguments));
//
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        Process process = processBuilder.start();
//
//
//        int exitCode = process.waitFor();
//
//        if (exitCode != 0) {
//            throw new RuntimeException("Shell script execution failed with exit code: " + exitCode);
//        }
//    }


    private static String exploreDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        exploreDirectory(file); // 디렉토리인 경우, 재귀적으로 탐색
                    } else {
                        return file.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }

    // PUT - a album
    @PutMapping(uri_album)
    public ResponseEntity<?> updateAlbum(
            @PathVariable(required = true) String album_id,
            @RequestBody AlbumModel model) {
        if (!parsePathVariablesOfAlbum(album_id))
            return ResponseEntity.badRequest().build();

        AlbumModel updatedModel = albumService.updateEntity(a_id, model);
//        log.info("model: " + updatedModel);
        ResponseDTO<AlbumModel> response = ResponseDTO.<AlbumModel>builder()
                .uri(getUri(uri_album))
                .success(updatedModel != null)
                .result(updatedModel)
                .build();

        return ResponseEntity.ok().body(response);
    }

    // DELETE - a album
    @DeleteMapping(uri_album)
    public ResponseEntity<?> deleteAlbum(
            @PathVariable(required = true) String album_id) {
        if (!parsePathVariablesOfAlbum(album_id))
            return ResponseEntity.badRequest().build();

        boolean deleted = albumService.deleteEntity(a_id);
        log.info("model: " + deleted);
        ResponseDTO<Boolean> response = ResponseDTO.<Boolean>builder()
                .uri(getUri(uri_album))
                .success(deleted)
                .result(deleted)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @PostMapping(uri_upload)
    public ResponseEntity<?> createAlbum(
            @RequestPart("file") MultipartFile file,
            @RequestPart("model") AlbumModel model) {

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        if (fileName == null || contentType == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isZip = fileName.endsWith(".zip") || contentType.equalsIgnoreCase("application/zip");
        boolean isTar = fileName.endsWith(".tar") || contentType.equalsIgnoreCase("application/x-tar");
        boolean isTgz = fileName.endsWith(".tgz") || contentType.equalsIgnoreCase("application/x-compressed-tar");

        if (!isZip && !isTar && !isTgz) {
            return ResponseEntity.badRequest().build();
        }

        AlbumModel createdModel = albumService.createEntity(model);
        albumService.saveCompressedFile(createdModel.getId(), file);

        ResponseDTO<AlbumModel> response = ResponseDTO.<AlbumModel>builder()
                .uri(apiTag)
                .success(true)
                .result(createdModel)
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(uri_test)
    public void download(@RequestBody FtpCredentialModel model) {
        String url = "/hdd_ext/part5/SIRIUS_REPOSITORY/LEMPStack/app/repo/album";
        String savePath = Paths.get(url,
                model.getUrl().substring(model.getUrl().indexOf("facility")+9),
                "3D_modeling").toString();
        String method = "download";
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
        try {
            String ftpRunPath = "/home/sb/Desktop/vsc/ftp_client/main.py";
            String[] ftpArgument = {"10.8.0.2", "29999", "sirius", "sbftp0617*",
                    method, "", savePath};
            ExecuteScript.executeShellScript("python", null, ftpRunPath, ftpArgument);
        } catch (IOException | InterruptedException e) {
            log.error("FTP " + method + " Error : " + e);
        }
//        });
//        executorService.shutdown(); // 스레드 풀 종료 시작
    }

    //    @PostMapping(uri_test)
//    public ResponseEntity<?> webhookListener(
//            @PathVariable Integer album_id,
//            @RequestBody FtpCredentialModel model) {
//        String url = "/hdd_ext/part5/SIRIUS_REPOSITORY/LEMPStack/app/repo/album";
//        String savePath = Paths.get(url,String.valueOf(album_id),"3D_modeling").toString().replace("\\","/");
//        String method = "download";
//        log.info("3d model : "+model);
////        ExecutorService executorService = Executors.newSingleThreadExecutor();
////        executorService.execute(() -> {
////        try {
////            String ftpRunPath = "/home/sb/Desktop/vsc/ftp_client/main.py";
////            String[] ftpArgument = {model.getFtpIp(), model.getFtpPort(), model.getFtpId(), model.getFtpPassword(),
////                    method, savePath, model.getUrl()};
////            ExecuteScript.executeShellScript("python3", null, ftpRunPath, ftpArgument);
////        } catch (IOException | InterruptedException e) {
////            log.error("FTP " + method + " Error : " + e);
////        }
////        });
////        executorService.shutdown(); // 스레드 풀 종료 시작
//        return ResponseEntity.ok().build();
//    }
}
