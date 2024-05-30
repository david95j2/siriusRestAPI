package com.sierrabase.siriusapi.service.modeling;

import com.sierrabase.siriusapi.entity.modeling.NetworkOfCrackEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelEntity;
import com.sierrabase.siriusapi.model.modeling.NetworkOfCrackModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModel;
import com.sierrabase.siriusapi.repository.modeling.NetworkOfCrackRepository;
import com.sierrabase.siriusapi.repository.modeling.ThreeDimensionalModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NetworkOfCrackService {
    @Autowired
    private NetworkOfCrackRepository networkOfCrackRepository;

    public ArrayList<NetworkOfCrackModel> getAllEntities(Integer tdmId)  {
        List<NetworkOfCrackEntity> entities = networkOfCrackRepository.findAllByThreeDimensionalModelId(tdmId);
        if(entities.size() <= 0)
            return null;

        ArrayList<NetworkOfCrackModel> modelList = new ArrayList<NetworkOfCrackModel>();
        for (NetworkOfCrackEntity entity : entities) {
            modelList.add(new NetworkOfCrackModel(entity));
        }

        return modelList;
    }

    public NetworkOfCrackModel getEntityById(Integer id) {
        Optional<NetworkOfCrackEntity> entity = networkOfCrackRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        NetworkOfCrackModel model = new NetworkOfCrackModel(entity.get());

        return model;
    }


    public NetworkOfCrackModel createEntity(NetworkOfCrackModel model) {
        NetworkOfCrackEntity entity = new NetworkOfCrackEntity(model);
        // Set properties from facilityModel to entity

        entity = networkOfCrackRepository.save(entity);
        return new NetworkOfCrackModel(entity);
    }

    public NetworkOfCrackModel updateEntity(Integer id, NetworkOfCrackModel model) {
        Optional<NetworkOfCrackEntity> optionalEntity = networkOfCrackRepository.findById(id);
        if (optionalEntity.isPresent()) {
            NetworkOfCrackEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = networkOfCrackRepository.save(new NetworkOfCrackEntity(entity.getNetwork_of_crack_id(), model));
            return new NetworkOfCrackModel(entity);
        } else {
            log.error("Network of crack for CAD not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<NetworkOfCrackEntity> optionalEntity = networkOfCrackRepository.findById(id);
        if (optionalEntity.isPresent()) {
            networkOfCrackRepository.deleteById(id);
            return true;
        } else {
            log.error("Network of crack for CAD Info not found with id: {}", id);
            return false;
        }
    }
}
