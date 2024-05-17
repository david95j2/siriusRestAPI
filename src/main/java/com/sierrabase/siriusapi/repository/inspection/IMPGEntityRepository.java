package com.sierrabase.siriusapi.repository.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPGEntity;
import com.sierrabase.siriusapi.entity.inspection.IMPWpsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMPGEntityRepository extends JpaRepository<IMPGEntity, Integer> {
    @Query("SELECT impge FROM IMPGEntity impge WHERE impge.imp_id = :impId")
    List<IMPGEntity> findAllByImpId(Integer impId);
}
