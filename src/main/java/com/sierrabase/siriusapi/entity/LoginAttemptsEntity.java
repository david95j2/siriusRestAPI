package com.sierrabase.siriusapi.entity;

import com.sierrabase.siriusapi.model.AuthorizeModel;
import com.sierrabase.siriusapi.model.LoginAttemptModel;
import com.sierrabase.siriusapi.model.album.AlbumPhotoModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="login_attempts")
public class LoginAttemptsEntity implements Serializable {
    @Id
    @Column(name = "attempt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attemptId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "attempted_login_id")
    private String attemptedLoginId;

    @Column(name = "attempted_password")
    private String attemptedPassword;

    @Column(name = "attempt_time")
    private ZonedDateTime attemptTime = ZonedDateTime.now();

    @Column(name = "success")
    private Boolean success;

    public LoginAttemptsEntity(final LoginAttemptModel model) {
        setAttemptId(model.getAttemptId());
        setUserId(model.getUserId());
        setAttemptedLoginId(model.getAttemptedLoginId());
        setAttemptedPassword(model.getAttemptedPassword());
        setSuccess(model.getSuccess());
    }
    public LoginAttemptsEntity(final Integer id, LoginAttemptModel model) {
        this(model);
        setAttemptId(id);
    }

}