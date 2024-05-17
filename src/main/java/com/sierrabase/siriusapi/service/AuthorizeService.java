package com.sierrabase.siriusapi.service;

import com.sierrabase.siriusapi.entity.UserEntity;
import com.sierrabase.siriusapi.model.AuthorizeModel;
import com.sierrabase.siriusapi.model.params.UserModel;
import com.sierrabase.siriusapi.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthorizeService {
    @Autowired
    private UserEntityRepository userEntityRepository;

    // getUsers --> getAllEntities
    public ArrayList<AuthorizeModel> getAllEntities() {
        List<UserEntity> entities = userEntityRepository.findAll();
        if (entities.size() <= 0)
            return null;

        ArrayList<AuthorizeModel> modelList = new ArrayList<AuthorizeModel>();
        for (UserEntity entity : entities) {
            modelList.add(new AuthorizeModel(entity));
        }

        return modelList;
    }

    public AuthorizeModel verify(AuthorizeModel authorizeModel) {
        // 파라미터인 lParams 정보를 가지고 사용자 정보를 확인해서 인증을 진행할 것
        Optional<UserEntity> entity = userEntityRepository.findByUserIdAndUserPw(authorizeModel.getUserId(),authorizeModel.getUserPw());
        if(!entity.isPresent())
            return null;

        AuthorizeModel model = new AuthorizeModel(entity.get());
        return model;
    }

    // registerUser --> createEntity
    public AuthorizeModel createEntity(UserModel userModel) {
        // 파라미터인 ruParams 정보를 가지고 사용자 등록을 진행할 것

        UserEntity entity = new UserEntity(new AuthorizeModel(userModel));

        entity = userEntityRepository.save(entity);
        return new AuthorizeModel(entity);
    }

    // getUserById --> getEntityById
    public AuthorizeModel getEntityById(Integer id) {
        // 사용자 ID에 대한 사용자 정보 반환할 것
        Optional<UserEntity> entity = userEntityRepository.findById(id);

        if(!entity.isPresent())
            return null;

        AuthorizeModel model = new AuthorizeModel(entity.get());

        return model;
    }

    public AuthorizeModel getDronesByUserId(Integer userId) {
        // 사용자 ID별 드론 정보 반환할 것
        return null;
    }

    public AuthorizeModel updateEntity(Integer id, AuthorizeModel authorizeModel) {
        Optional<UserEntity> optionalEntity = userEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            UserEntity entity = optionalEntity.get();
            // Update properties from facilityModel to entity

            entity = userEntityRepository.save(new UserEntity(entity.getUser_id(), authorizeModel));
            return new AuthorizeModel(entity);
        } else {
            log.error("User not found with id: {}", id);
            return null;
        }
    }

    public boolean deleteEntity(Integer id) {
        Optional<UserEntity> optionalEntity = userEntityRepository.findById(id);
        if (optionalEntity.isPresent()) {
            userEntityRepository.deleteById(id);
            return true;
        } else {
            log.error("User not found with id : {}", id);
            return false;
        }
    }

    public Boolean checkForDuplicateUserId(String userId) {
        Optional<UserEntity> entity = userEntityRepository.findByUserId(userId);

        if(!entity.isPresent())
            return false;

        return true;
    }
}