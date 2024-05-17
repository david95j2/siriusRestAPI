package com.sierrabase.siriusapi.service.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPEntity;
import com.sierrabase.siriusapi.entity.inspection.IMPGEntity;
import com.sierrabase.siriusapi.model.inspection.FitAreaProgramModel;
import com.sierrabase.siriusapi.model.inspection.IMPGModel;
import com.sierrabase.siriusapi.model.inspection.IMPModel;
import com.sierrabase.siriusapi.repository.inspection.IMPGEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class IMPGService {
    @Autowired
    private IMPGEntityRepository impgEntityRepository;

    public ArrayList<IMPGModel> getAllEntitiesByImpId(Integer impId) {
        List<IMPGEntity> entities = impgEntityRepository.findAllByImpId(impId);

        if(entities.size() <= 0)
            return null;

        ArrayList<IMPGModel> modelList = new ArrayList<IMPGModel>();
        for (IMPGEntity entity : entities) {
            modelList.add(new IMPGModel(entity));
        }

        return modelList;
    }

    public IMPGModel getEntityById(Integer id) {
        Optional<IMPGEntity> entity = impgEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        IMPGModel model = new IMPGModel(entity.get());

        return model;
    }

    public IMPGModel createEntity(IMPGModel impgModel) {
        IMPGEntity entity = new IMPGEntity(impgModel);
        // Set properties from impModel to entity

        entity = impgEntityRepository.save(entity);
        return new IMPGModel(entity);
    }

    public IMPGModel updateEntity(Integer id, IMPGModel impgModel) {
        Optional<IMPGEntity> optionalEntity = impgEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            IMPGEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = impgEntityRepository.save(new IMPGEntity(entity.getImpg_id(), impgModel));
            return new IMPGModel(entity);
        } else {
            log.error("IMPG not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<IMPGEntity> optionalEntity = impgEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            impgEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("IMPG not found with id: {}", id);
            return false;
        }
    }
}