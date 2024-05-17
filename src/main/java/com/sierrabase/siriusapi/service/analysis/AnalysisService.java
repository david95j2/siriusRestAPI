package com.sierrabase.siriusapi.service.analysis;


import com.sierrabase.siriusapi.entity.analysis.AnalysisEntity;
import com.sierrabase.siriusapi.model.analysis.AnalysisModel;
import com.sierrabase.siriusapi.repository.analysis.AnalysisEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AnalysisService {
    @Autowired
    private AnalysisEntityRepository analysisEntityRepository;

    public ArrayList<AnalysisModel> getAllEnitities(Integer id) {
        List<AnalysisEntity> entities =  analysisEntityRepository.findByAlbumId(id);

        if(entities.size() <= 0)
            return null;

        ArrayList<AnalysisModel> modelList = new ArrayList<AnalysisModel>();
        for (AnalysisEntity entity : entities) {
            modelList.add(new AnalysisModel(entity));
        }

        return modelList;
    }

    public AnalysisModel getEntityById(Integer analysisId) {
        Optional<AnalysisEntity> entity = analysisEntityRepository.findById(analysisId);

        if(!entity.isPresent())
            return null;

        AnalysisModel model = new AnalysisModel(entity.get());

        return model;
    }

    public AnalysisModel getEntityByCondition(Integer analysisId, Integer albumId) {
        Optional<AnalysisEntity> entity = analysisEntityRepository.findByIdAndAlbumId(analysisId, albumId);

        if(!entity.isPresent())
            return null;

        AnalysisModel model = new AnalysisModel(entity.get());

        return model;
    }

    public AnalysisModel createEntity(AnalysisModel analysisModel) {
        AnalysisEntity entity = new AnalysisEntity(analysisModel);
        // Set properties from albumPhotoModel to entity
        entity = analysisEntityRepository.save(entity);
        return new AnalysisModel(entity);
    }

    public AnalysisModel updateEntity(Integer id, AnalysisModel analysisModel) {
        Optional<AnalysisEntity> optionalEntity = analysisEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            AnalysisEntity entity = optionalEntity.get();
            // Update properties from albumPhotoModel to entity

            entity = analysisEntityRepository.save(new AnalysisEntity(entity.getAnalysis_id(), analysisModel));
            return new AnalysisModel(entity);
        } else {
            log.error("Analysis not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<AnalysisEntity> optionalEntity = analysisEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            analysisEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("Analysis not found with id: {}", id);
            return false;
        }
    }


}