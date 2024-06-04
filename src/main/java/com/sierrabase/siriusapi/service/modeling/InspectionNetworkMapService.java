package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.entity.modeling.inspectionNetworkMapEntity;
import com.sierrabase.siriusapi.model.modeling.InspectionNetworkMapModel;
import com.sierrabase.siriusapi.repository.modeling.InspectionNetworkMapEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InspectionNetworkMapService {
    @Autowired
    private InspectionNetworkMapEntityRepository inspectionNetworkMapEntityRepository;

    public ArrayList<InspectionNetworkMapModel> getAllEntities(Integer tdmId)  {
        List<inspectionNetworkMapEntity> entities = inspectionNetworkMapEntityRepository.findAllByThreeDimensionalModelId(tdmId);
        if(entities.size() <= 0)
            return null;

        ArrayList<InspectionNetworkMapModel> modelList = new ArrayList<InspectionNetworkMapModel>();
        for (inspectionNetworkMapEntity entity : entities) {
            modelList.add(new InspectionNetworkMapModel(entity));
        }

        return modelList;
    }

    public InspectionNetworkMapModel getEntityById(Integer id) {
        Optional<inspectionNetworkMapEntity> entity = inspectionNetworkMapEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        InspectionNetworkMapModel model = new InspectionNetworkMapModel(entity.get());

        return model;
    }


    public InspectionNetworkMapModel createEntity(InspectionNetworkMapModel model) {
        inspectionNetworkMapEntity entity = new inspectionNetworkMapEntity(model);
        // Set properties from facilityModel to entity

        entity = inspectionNetworkMapEntityRepository.save(entity);
        return new InspectionNetworkMapModel(entity);
    }

    public InspectionNetworkMapModel updateEntity(Integer id, InspectionNetworkMapModel model) {
        Optional<inspectionNetworkMapEntity> optionalEntity = inspectionNetworkMapEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            inspectionNetworkMapEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = inspectionNetworkMapEntityRepository.save(new inspectionNetworkMapEntity(entity.getInspection_network_map_id(), model));
            return new InspectionNetworkMapModel(entity);
        } else {
            log.error("Network of crack for CAD not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<inspectionNetworkMapEntity> optionalEntity = inspectionNetworkMapEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            inspectionNetworkMapEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("Network of crack for CAD Info not found with id: {}", id);
            return false;
        }
    }
}
