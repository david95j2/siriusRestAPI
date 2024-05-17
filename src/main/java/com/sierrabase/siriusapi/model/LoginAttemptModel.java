package com.sierrabase.siriusapi.model;

import com.sierrabase.siriusapi.entity.LoginAttemptsEntity;
import com.sierrabase.siriusapi.entity.UserEntity;
import com.sierrabase.siriusapi.model.params.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class LoginAttemptModel {
    private int attemptId;
    private int userId;
    private String attemptedLoginId;
    private String attemptedPassword;
    private ZonedDateTime attemptTime;
    private Boolean success;

    public LoginAttemptModel(final LoginAttemptsEntity entity) {
        setAttemptId(entity.getAttemptId());
        setUserId(entity.getUserId());
        setAttemptedLoginId(entity.getAttemptedLoginId());
        setAttemptedPassword(entity.getAttemptedPassword());
        setAttemptTime(entity.getAttemptTime());
        setSuccess(entity.getSuccess());
    }

    public LoginAttemptModel(final AuthorizeModel model,final boolean success) {
        setUserId(model.getId());
        setAttemptedLoginId(model.getUserId());
        setAttemptedPassword(model.getUserPw());
        setSuccess(success);
    }
}
