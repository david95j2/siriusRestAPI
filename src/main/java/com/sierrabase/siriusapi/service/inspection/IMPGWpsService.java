package com.sierrabase.siriusapi.service.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPWpsEntity;
import com.sierrabase.siriusapi.model.inspection.IMPWpsModel;
import com.sierrabase.siriusapi.repository.inspection.IMPWpsEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class IMPGWpsService {
    @Autowired
    private IMPWpsEntityRepository impWpsEntityRepository;

    public ArrayList<IMPWpsModel> getAllEntitiesByImpIdAndImpgId(int iId, int igId) {
        List<IMPWpsEntity> entities = impWpsEntityRepository.findAllByImpIdAndImpgId(iId, igId);

        if (entities.size() <= 0)
            return null;

        ArrayList<IMPWpsModel> modelList = new ArrayList<IMPWpsModel>();
        for (IMPWpsEntity entity : entities) {
            modelList.add(new IMPWpsModel(entity));
        }

        return modelList;
    }

    public ArrayList<IMPWpsModel> createEntities(ArrayList<IMPWpsModel> impWpsModelArrayList) {
        ArrayList<IMPWpsModel> waypoints = new ArrayList<IMPWpsModel>();
        for (IMPWpsModel impWpsModel : impWpsModelArrayList) {
            IMPWpsEntity entity = new IMPWpsEntity(impWpsModel);
            // Set properties from waypointModel to entity

            entity = impWpsEntityRepository.save(entity);
            waypoints.add(new IMPWpsModel(entity));
        }

        return waypoints.size() == 0 ? null : waypoints;
    }

    public ArrayList<IMPWpsModel> updateEntities(Integer iId, Integer igId, ArrayList<IMPWpsModel> impWpsModelArrayList) {
        deleteEntities(iId, igId);

        return createEntities(impWpsModelArrayList);
    }

    public Integer deleteEntities(Integer iId, Integer igId) {
        return impWpsEntityRepository.deleteAllByImpgId(iId, igId);
    }
}
