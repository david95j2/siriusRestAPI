package com.sierrabase.siriusapi.repository.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPWpsEntity;
import com.sierrabase.siriusapi.entity.inspection.IMPWpsMissionCameraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMPWpsMissionCameraEntityRepository extends JpaRepository<IMPWpsMissionCameraEntity, Integer> {
    @Query("SELECT impwmce FROM IMPWpsMissionCameraEntity impwmce WHERE impwmce.imp_id = :impId")
    List<IMPWpsMissionCameraEntity> findAllByImpId(Integer impId);

    @Transactional
    @Modifying
    @Query("DELETE FROM IMPWpsMissionCameraEntity impwmce WHERE impwmce.imp_id = :impId")
    Integer deleteAllByImpId(Integer impId);
}
