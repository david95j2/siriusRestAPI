package com.sierrabase.siriusapi.service.inspection;


import com.sierrabase.siriusapi.entity.inspection.IMPWpsMissionCameraEntity;

import com.sierrabase.siriusapi.model.inspection.IMPWpsMissionCameraModel;

import com.sierrabase.siriusapi.repository.inspection.IMPWpsMissionCameraEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class IMPWpsMissionCameraService {
    @Autowired
    private IMPWpsMissionCameraEntityRepository impWpsMissionCameraEntityRepository;

    public ArrayList<IMPWpsMissionCameraModel> getAllEntitiesByImpId(Integer mId) {
        List<IMPWpsMissionCameraEntity> entities = impWpsMissionCameraEntityRepository.findAllByImpId(mId);

        if (entities.size() <= 0)
            return null;

        ArrayList<IMPWpsMissionCameraModel> modelList = new ArrayList<IMPWpsMissionCameraModel>();
        for (IMPWpsMissionCameraEntity entity : entities) {
            modelList.add(new IMPWpsMissionCameraModel(entity));
        }

        return modelList;
    }

    public ArrayList<IMPWpsMissionCameraModel> createEntities(ArrayList<IMPWpsMissionCameraModel> models) {
        ArrayList<IMPWpsMissionCameraModel> waypoints = new ArrayList<IMPWpsMissionCameraModel>();
        for (IMPWpsMissionCameraModel impWpsMCM : models) {

            IMPWpsMissionCameraEntity entity = new IMPWpsMissionCameraEntity(impWpsMCM);

            // Set properties from waypointModel to entity
            entity = impWpsMissionCameraEntityRepository.save(entity);
            waypoints.add(new IMPWpsMissionCameraModel(entity));
        }

        return waypoints.size() == 0 ? null : waypoints;
    }

    public ArrayList<IMPWpsMissionCameraModel> updateEntities(Integer impId, ArrayList<IMPWpsMissionCameraModel> models) {
        deleteEntities(impId);

        return createEntities(models);
    }

    public Integer deleteEntities(Integer impId) {
        return impWpsMissionCameraEntityRepository.deleteAllByImpId(impId);
    }
}