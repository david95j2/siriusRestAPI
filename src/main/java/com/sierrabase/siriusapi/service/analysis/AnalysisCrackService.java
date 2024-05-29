package com.sierrabase.siriusapi.service.analysis;

import com.sierrabase.siriusapi.common.URICreator;
import com.sierrabase.siriusapi.entity.album.AlbumEntity;
import com.sierrabase.siriusapi.entity.album.AlbumPhotoEntity;
import com.sierrabase.siriusapi.entity.analysis.AnalysisCrackEntity;

import com.sierrabase.siriusapi.model.album.AlbumModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import com.sierrabase.siriusapi.model.analysis.AnalysisCrackModel;
import com.sierrabase.siriusapi.model.analysis.JsonModel;
import com.sierrabase.siriusapi.repository.album.AlbumEntityRepository;
import com.sierrabase.siriusapi.repository.album.AlbumPhotoPosEntityRepository;
import com.sierrabase.siriusapi.repository.analysis.AnalysisCrackEntityRepository;
import com.sierrabase.siriusapi.service.album.AlbumPhotoService;
import com.sierrabase.siriusapi.service.album.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AnalysisCrackService {

    @Autowired
    private AlbumPhotoService albumPhotoService;
    @Autowired
    private AlbumEntityRepository albumEntityRepository;
    @Autowired
    private AnalysisCrackEntityRepository analysisCrackEntityRepository;
    @Autowired
    private AlbumPhotoPosEntityRepository albumPhotoPosEntityRepository;
    @Autowired
    private AnalysisCrackWorker analysisCrackWorker;
    @Value("${path.repository.base}")
    private String repository_path;

    @Value("${path.worker.base}")
    private String worker_path;
    public ArrayList<AnalysisCrackModel> getAllEntities(Integer albumId, Integer id) {
        List<AnalysisCrackEntity> entities =  analysisCrackEntityRepository.findByAnalysisId(id);

        if(entities.size() <= 0)
            return null;
        ArrayList<AnalysisCrackModel> modelList = new ArrayList<AnalysisCrackModel>();
        for (AnalysisCrackEntity entity : entities) {
            String onlyFileName = Paths.get(entity.getCracksInfoPath()).getFileName().toString();
            onlyFileName = onlyFileName.substring(0,26)+"%";
            List<AlbumPhotoEntity> result = albumPhotoPosEntityRepository.findByPattern(albumId, onlyFileName);

            // 2이상이면 안됨...
            for (AlbumPhotoEntity temp : result) {
                modelList.add(new AnalysisCrackModel(temp.getAlbum_photo_id(), entity));
            }
        }

        return modelList;
    }

    public AnalysisCrackModel getEntityById(Integer albumId, Integer id) {
        Optional<AnalysisCrackEntity> entity = analysisCrackEntityRepository.findById(id);

        if(!entity.isPresent())
            return null;

        String onlyFileName = Paths.get(entity.get().getCracksInfoPath()).getFileName().toString();
        onlyFileName = onlyFileName.substring(0,26)+"%";
        List<AlbumPhotoEntity> result = albumPhotoPosEntityRepository.findByPattern(albumId, onlyFileName);

        AnalysisCrackModel model = new AnalysisCrackModel();
        for (AlbumPhotoEntity temp : result) {
            model = new AnalysisCrackModel(temp.getAlbum_photo_id(), entity.get());
        }

        return model;
    }

    public AnalysisCrackModel createEntity(AnalysisCrackModel analysisCrackModel) {
        AnalysisCrackEntity entity = new AnalysisCrackEntity(analysisCrackModel);
        // Set properties from analysisCrack to entity
        entity = analysisCrackEntityRepository.save(entity);
        return new AnalysisCrackModel(entity);
    }

    public AnalysisCrackModel updateEntity(Integer id, AnalysisCrackModel analysisCrackModel) {
        Optional<AnalysisCrackEntity> optionalEntity = analysisCrackEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            AnalysisCrackEntity entity = optionalEntity.get();

            // Update properties from analysisCrack to entity
            entity = analysisCrackEntityRepository.save(new AnalysisCrackEntity(entity.getAnalysis_crack_id(), analysisCrackModel));
            return new AnalysisCrackModel(entity);
        } else {
            log.error("AnalysisCrack not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<AnalysisCrackEntity> optionalEntity = analysisCrackEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            analysisCrackEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("AnalysisCrack not found with id: {}", id);
            return false;
        }
    }


    public boolean modifyAnalysisCrack(JsonModel updatedJson, AnalysisCrackModel model) {
        String sourcePath = URICreator.pathToString(repository_path, model.getCracksInfoPath().substring(model.getCracksInfoPath().indexOf("/album")));

        // 파일 저장
        boolean saveResult = analysisCrackWorker.saveFile(sourcePath, updatedJson);
        if (!saveResult) {
            return false;
        }


        AlbumPhotoModel albumPhotoInfo = albumPhotoService.getEntityById(model.getPhotoId());
        if (albumPhotoInfo == null) {
            log.error("albumPhoto not exists");
            return false;
        }

        Optional<AlbumEntity> entity = albumEntityRepository.findById(model.getAlbumId());

        if(!entity.isPresent()) {
            log.error("albumEntity not found!");
            return false;
        }

        AlbumModel albumModel = new AlbumModel(entity.get());
        Integer facilityId = albumModel.getFacilityId();
        Integer facilityMapId = albumModel.getFacilityMapId();

        // 균열 별 이격거리 계산
        String calDisResult = analysisCrackWorker.computeCrackToCameraDistanceForFile(facilityId,facilityMapId, albumPhotoInfo);
        if(calDisResult == null) {
            log.error("Compute Crack To Camera Distances modify Error");
            return false;
        }
        
        return analysisCrackWorker.resetIndexForFile(calDisResult);
    }
}