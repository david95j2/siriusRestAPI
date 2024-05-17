package com.sierrabase.siriusapi.repository.inspection.shape;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeUndersidePropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IMPGShapeUndersidePropertyEntityRepository extends JpaRepository<IMPGShapeUndersidePropertyEntity, Integer> {
    @Query("SELECT impgsupe FROM IMPGShapeUndersidePropertyEntity impgsupe WHERE impgsupe.impg_shape_id=:impgShapeId")
    Optional<IMPGShapeUndersidePropertyEntity> findIMPGShapeUndersidePropertyEntityByImpgShapeId(Integer impgShapeId);
}
