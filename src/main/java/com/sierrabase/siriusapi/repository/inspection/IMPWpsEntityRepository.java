package com.sierrabase.siriusapi.repository.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPWpsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMPWpsEntityRepository extends JpaRepository<IMPWpsEntity, Integer> {
    @Query("SELECT impwe FROM IMPWpsEntity impwe WHERE impwe.imp_id = :impId")
    List<IMPWpsEntity> findAllByImpId(Integer impId);

    @Transactional
    @Modifying
    @Query("DELETE FROM IMPWpsEntity impwe WHERE impwe.imp_id = :impId")
    Integer deleteAllByImpId(Integer impId);

    @Query("SELECT impwe FROM IMPWpsEntity impwe WHERE impwe.imp_id = :impId And impwe.impg_id = :impgId")
    List<IMPWpsEntity> findAllByImpIdAndImpgId(Integer impId, Integer impgId);

    @Transactional
    @Modifying
    @Query("DELETE FROM IMPWpsEntity impwe WHERE impwe.imp_id = :impId And impwe.impg_id = :impgId")
    Integer deleteAllByImpgId(Integer impId, Integer impgId);
}
