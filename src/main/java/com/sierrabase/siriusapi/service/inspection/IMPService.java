package com.sierrabase.siriusapi.service.inspection;

import com.sierrabase.siriusapi.entity.FacilityEntity;
import com.sierrabase.siriusapi.entity.inspection.IMPEntity;
import com.sierrabase.siriusapi.model.FacilityModel;
import com.sierrabase.siriusapi.model.inspection.FitAreaProgramModel;
import com.sierrabase.siriusapi.model.inspection.IMPModel;
import com.sierrabase.siriusapi.model.mapping.MappingMissionModel;
import com.sierrabase.siriusapi.repository.inspection.IMPEntityRepository;
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
public class IMPService {
    @Autowired
    private IMPEntityRepository impEntityRepository;
    @Autowired
    private ImpWorker impWorker;

    public ArrayList<IMPModel> getAllEntitiesByFacilityMapId(int fMapId) {
        List<IMPEntity> entities = impEntityRepository.findAllByFacilityMapId(fMapId);

        if(entities.size() <= 0)
            return null;

        ArrayList<IMPModel> modelList = new ArrayList<IMPModel>();
        for (IMPEntity entity : entities) {
            modelList.add(new IMPModel(entity));
        }

        return modelList;
    }

    public IMPModel getEntityById(Integer id) {
        Optional<IMPEntity> entity = impEntityRepository.findById(id);

        log.debug("entity", entity);

        if(!entity.isPresent())
            return null;

        IMPModel model = new IMPModel(entity.get());

        return model;
    }

    public IMPModel createEntity(IMPModel impModel) {
        IMPEntity entity = new IMPEntity(impModel);
        // Set properties from impModel to entity

        entity = impEntityRepository.save(entity);
        return new IMPModel(entity);
    }

    public IMPModel updateEntity(Integer id, IMPModel impModel) {
        Optional<IMPEntity> optionalEntity = impEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            IMPEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = impEntityRepository.save(new IMPEntity(entity.getImp_id(), impModel));
            return new IMPModel(entity);
        } else {
            log.error("IMP not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<IMPEntity> optionalEntity = impEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            impEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("IMP not found with id: {}", id);
            return false;
        }
    }

    public boolean createPathPlanning(Integer facilityId, Integer facilityMapId, FitAreaProgramModel model) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            boolean result = impWorker.createPathPlanning(facilityId,facilityMapId,model);
            if (!result) {
                log.error("Can not create path plan");
            }
        });
        executorService.shutdown(); // 스레드 풀 종료 시작
        return true;
    }
}