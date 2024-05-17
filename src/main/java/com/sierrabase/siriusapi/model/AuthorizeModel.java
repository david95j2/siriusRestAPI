package com.sierrabase.siriusapi.model;

import com.sierrabase.siriusapi.entity.UserEntity;
import com.sierrabase.siriusapi.model.params.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class AuthorizeModel {
//    ArrayList<String> users;
//    public AuthorizeModel(final entity )


    private int id;
    private String userId;
    private String userPw;
    private boolean isInitialPassword;
    private ZonedDateTime createdDatetime;
    private ZonedDateTime wrDatetime;

    public AuthorizeModel(final UserEntity entity) {
        setId(entity.getUser_id());
        setUserId(entity.getUser_login_id());
        setUserPw(entity.getUser_password());
        setInitialPassword(entity.isInitialPassword());
        setCreatedDatetime(entity.getCreated_datetime());
        setWrDatetime(entity.getWr_datetime());
    }

    public AuthorizeModel(final UserModel userModel) {
        setUserId(userModel.getUserId());
        setUserPw(userModel.getUserPw());
    }
}
