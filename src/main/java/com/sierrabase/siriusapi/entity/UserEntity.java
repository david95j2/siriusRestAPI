package com.sierrabase.siriusapi.entity;

import com.sierrabase.siriusapi.model.AuthorizeModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="user")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private int user_id;
    @Column(name = "user_login_id")
    private String user_login_id;
    @Column(name = "user_password")
    private String user_password;
    @Column(name = "is_initial_password")
    private boolean isInitialPassword;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public UserEntity(final AuthorizeModel model) {
        setUser_id(model.getId());
        setUser_login_id(model.getUserId());
        setUser_password(model.getUserPw());
        setCreated_datetime(model.getCreatedDatetime());
        setWr_datetime(model.getWrDatetime());
    }

    public UserEntity(final Integer id, final AuthorizeModel model) {
        this(model);
        setUser_id(id);
    }
}