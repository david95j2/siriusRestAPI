package com.sierrabase.siriusapi.repository;

import com.sierrabase.siriusapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    @Query("select user from UserEntity user where user.user_login_id = :userId and user.user_password = :userPw")
    Optional<UserEntity> findByUserIdAndUserPw(String userId, String userPw);

    @Query("select user from UserEntity user where user.user_login_id = :userId")
    Optional<UserEntity> findByUserId(String userId);
}
