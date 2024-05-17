package com.sierrabase.siriusapi.service.mapping;

import com.sierrabase.siriusapi.entity.mapping.MappingMissionWaypointsEntity;
import com.sierrabase.siriusapi.model.mapping.MappingMissionWaypointsModel;
import com.sierrabase.siriusapi.repository.mapping.MappingMissionWaypointsEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MappingMissionWaypointsService {
    @Autowired
    private MappingMissionWaypointsEntityRepository mappingMissionWaypointsEntityRepository;

    public ArrayList<MappingMissionWaypointsModel> getAllEntitiesByMappingMissionId(int mId) {
        List<MappingMissionWaypointsEntity> entities = mappingMissionWaypointsEntityRepository.findAllByMappingMissionId(mId);

        if (entities.size() <= 0)
            return null;

        ArrayList<MappingMissionWaypointsModel> modelList = new ArrayList<MappingMissionWaypointsModel>();
        for (MappingMissionWaypointsEntity entity : entities) {
            modelList.add(new MappingMissionWaypointsModel(entity));
        }

        return modelList;
    }

    public ArrayList<MappingMissionWaypointsModel> createEntities(ArrayList<MappingMissionWaypointsModel> mappingMissionWaypointsModelArrayList) {
        ArrayList<MappingMissionWaypointsModel> waypoints = new ArrayList<MappingMissionWaypointsModel>();
        for (MappingMissionWaypointsModel waypointsModel : mappingMissionWaypointsModelArrayList) {
            MappingMissionWaypointsEntity entity = new MappingMissionWaypointsEntity(waypointsModel);
            // Set properties from waypointModel to entity

            entity = mappingMissionWaypointsEntityRepository.save(entity);
            waypoints.add(new MappingMissionWaypointsModel(entity));
        }

        return waypoints.size() == 0 ? null : waypoints;
    }

    public ArrayList<MappingMissionWaypointsModel> updateEntities(Integer mId, ArrayList<MappingMissionWaypointsModel> mappingMissionWaypointsModelArrayList) {
        deleteEntities(mId);

        return createEntities(mappingMissionWaypointsModelArrayList);
    }

    public Integer deleteEntities(Integer mId) {
//        List<MappingMissionWaypointsEntity> entities = mappingMissionWaypointsEntityRepository.findAllByMappingMissionId(mId);

//        for (MappingMissionWaypointsEntity entity : entities) {
//            mappingMissionWaypointsEntityRepository.deleteById(entity.getMapping_mission_waypoints_id());
//        }
        return mappingMissionWaypointsEntityRepository.deleteAllByMappingMissionId(mId);
    }
}
