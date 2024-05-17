package com.sierrabase.siriusapi.repository;

import com.sierrabase.siriusapi.entity.DroneSystemLogEntity;
import com.sierrabase.siriusapi.entity.LoginAttemptsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DroneSystemLogEntityRepository extends JpaRepository<DroneSystemLogEntity, Integer> {

}
