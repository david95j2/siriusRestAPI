package com.sierrabase.siriusapi.repository.inspection.shape;

import com.sierrabase.siriusapi.entity.inspection.IMPGEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IMPGShapeEntityRepository extends JpaRepository<IMPGShapeEntity, Integer> {
    @Query("SELECT impgs FROM IMPGShapeEntity impgs WHERE impgs.imp_id = :impId")
    List<IMPGShapeEntity> findAllByImpId(Integer impId);
}
