package com.sierrabase.siriusapi.repository.inspection.shape;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeLinePropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IMPGShapeLinePropertyEntityRepository extends JpaRepository<IMPGShapeLinePropertyEntity, Integer> {
    @Query("SELECT impglrpe FROM IMPGShapeLinePropertyEntity impglrpe WHERE impglrpe.impg_shape_id = :impgShapeId")
    Optional<IMPGShapeLinePropertyEntity> findIMPGShapeLinePropertyEntityByImpgShapeId(Integer impgShapeId);
}
