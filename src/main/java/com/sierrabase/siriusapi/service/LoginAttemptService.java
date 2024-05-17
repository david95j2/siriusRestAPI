package com.sierrabase.siriusapi.service;

import com.sierrabase.siriusapi.entity.FacilityMapEntity;
import com.sierrabase.siriusapi.entity.LoginAttemptsEntity;
import com.sierrabase.siriusapi.model.FacilityMapModel;
import com.sierrabase.siriusapi.model.LoginAttemptModel;
import com.sierrabase.siriusapi.repository.FacilityMapEntityRepository;
import com.sierrabase.siriusapi.repository.LoginAttemptEntityRepository;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LoginAttemptService {
    @Autowired
    private LoginAttemptEntityRepository loginAttemptEntityRepository;

    public ArrayList<LoginAttemptModel> getEntitiesByFacilityId(Integer userId) {
        List<LoginAttemptsEntity> entities = loginAttemptEntityRepository.findAllByUserId(userId);

        if (entities.size() <= 0)
            return null;

        ArrayList<LoginAttemptModel> modelList = new ArrayList<LoginAttemptModel>();
        for (LoginAttemptsEntity entity : entities) {
            modelList.add(new LoginAttemptModel(entity));
        }

        return modelList;
    }

    public LoginAttemptModel getEntityById(Integer id) {
        Optional<LoginAttemptsEntity> entity = loginAttemptEntityRepository.findById(id);

        if (!entity.isPresent())
            return null;

        LoginAttemptModel model = new LoginAttemptModel(entity.get());

        return model;
    }

    public LoginAttemptModel createEntity(LoginAttemptModel model) {
        LoginAttemptsEntity entity = new LoginAttemptsEntity(model);
        // Set properties from facilityMapModel to entity

        entity = loginAttemptEntityRepository.save(entity);
        return new LoginAttemptModel(entity);
    }

    public LoginAttemptModel updateEntity(Integer id, LoginAttemptModel model) {
        Optional<LoginAttemptsEntity> optionalEntity = loginAttemptEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            LoginAttemptsEntity entity = optionalEntity.get();
            // Update properties from facilityMapModel to entity

            entity = loginAttemptEntityRepository.save(new LoginAttemptsEntity(entity.getAttemptId(), model));
            return new LoginAttemptModel(entity);
        } else {
            log.error("LoginAttempts not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<LoginAttemptsEntity> optionalEntity = loginAttemptEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            loginAttemptEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("LoginAttempts not found with id: {}", id);
            return false;
        }
    }
}
