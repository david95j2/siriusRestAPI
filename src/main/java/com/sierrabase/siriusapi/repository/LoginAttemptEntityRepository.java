package com.sierrabase.siriusapi.repository;

import com.sierrabase.siriusapi.entity.LoginAttemptsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoginAttemptEntityRepository extends JpaRepository<LoginAttemptsEntity, Integer> {
@Query("SELECT la FROM LoginAttemptsEntity la WHERE la.userId = :userId")
    List<LoginAttemptsEntity> findAllByUserId(Integer userId);
}
