package com.sierrabase.siriusapi.repository.inspection.shape;

import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCirclePropertyEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeRectanglePropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IMPGShapeCirclePropertyEntityRepository extends JpaRepository<IMPGShapeCirclePropertyEntity, Integer> {
    @Query("SELECT impgscpe FROM IMPGShapeCirclePropertyEntity impgscpe WHERE impgscpe.impg_shape_id = :impgShapeId")
    Optional<IMPGShapeCirclePropertyEntity> findIMPGShapeCirclePropertyEntityByImpgShapeId(Integer impgShapeId);


}
