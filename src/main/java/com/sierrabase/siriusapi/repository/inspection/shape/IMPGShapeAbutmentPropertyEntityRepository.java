package com.sierrabase.siriusapi.repository.inspection.shape;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeAbutmentPropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IMPGShapeAbutmentPropertyEntityRepository extends JpaRepository<IMPGShapeAbutmentPropertyEntity, Integer> {
    @Query("SELECT impgsape FROM IMPGShapeAbutmentPropertyEntity impgsape WHERE impgsape.impg_shape_id = :impgShapeId")
    Optional<IMPGShapeAbutmentPropertyEntity> findIMPGShapeAbutmentPropertyEntityByImpgShapeId(Integer impgShapeId);
}
