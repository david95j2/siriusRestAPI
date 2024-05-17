package com.sierrabase.siriusapi.repository.inspection.shape;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeRectanglePropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IMPGShapeRectanglePropertyEntityRepository extends JpaRepository<IMPGShapeRectanglePropertyEntity, Integer> {
    @Query("SELECT impgsrpe FROM IMPGShapeRectanglePropertyEntity impgsrpe WHERE impgsrpe.impg_shape_id = :impgShapeId")
    Optional<IMPGShapeRectanglePropertyEntity> findIMPGShapeRectanglePropertyEntityByImpgShapeId(Integer impgShapeId);


}
