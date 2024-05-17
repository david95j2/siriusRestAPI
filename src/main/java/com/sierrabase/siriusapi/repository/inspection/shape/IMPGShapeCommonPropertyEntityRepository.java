package com.sierrabase.siriusapi.repository.inspection.shape;

import com.sierrabase.siriusapi.entity.inspection.shape.IMPGShapeEntity;
import com.sierrabase.siriusapi.entity.inspection.shape.property.IMPGShapeCommonPropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IMPGShapeCommonPropertyEntityRepository extends JpaRepository<IMPGShapeCommonPropertyEntity, Integer> {
    @Query("SELECT impgscpe FROM IMPGShapeCommonPropertyEntity impgscpe WHERE impgscpe.impg_shape_id = :impgShapeId")
    Optional<IMPGShapeCommonPropertyEntity> findIMPGShapeCommonPropertyEntityByImpgShapeId(Integer impgShapeId);

}
