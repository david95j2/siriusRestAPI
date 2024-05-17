package com.sierrabase.siriusapi.service.mapping;

import com.sierrabase.siriusapi.entity.mapping.MappingMissionEntity;
import com.sierrabase.siriusapi.model.mapping.MappingMissionModel;
import com.sierrabase.siriusapi.repository.mapping.MappingMissionEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MappingMissionService {

    @Autowired
    private MappingMissionEntityRepository mappingMissionEntityRepository;

    public ArrayList<MappingMissionModel> getAllEntitiesByFacilityMapId(int fMapId) {
        List<MappingMissionEntity> entities = mappingMissionEntityRepository.findAllByFacilityMapId(fMapId);

        if(entities.size() <= 0)
            return null;

        ArrayList<MappingMissionModel> modelList = new ArrayList<MappingMissionModel>();
        for (MappingMissionEntity entity : entities) {
            modelList.add(new MappingMissionModel(entity));
        }

        return modelList;
    }

    public MappingMissionModel getEntityById(Integer id) {
        Optional<MappingMissionEntity> entity = mappingMissionEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        MappingMissionModel model = new MappingMissionModel(entity.get());

        return model;

    }

    public MappingMissionModel createEntity(MappingMissionModel missionModel) {
        MappingMissionEntity entity = new MappingMissionEntity(missionModel);
        // Set properties from missionModel to entity

        entity = mappingMissionEntityRepository.save(entity);
        return new MappingMissionModel(entity);
    }

    public MappingMissionModel updateEntity(Integer id, MappingMissionModel missionModel) {
        Optional<MappingMissionEntity> optionalEntity = mappingMissionEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            MappingMissionEntity entity = optionalEntity.get();
            // Update properties from missionModel to entity

            entity = mappingMissionEntityRepository.save(new MappingMissionEntity(entity.getMapping_mission_id(), missionModel));
            return new MappingMissionModel(entity);
        } else {
            log.error("MappingMission not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<MappingMissionEntity> optionalEntity = mappingMissionEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            mappingMissionEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("MappingMission not found with id: {}", id);
            return false;
        }
    }
}
