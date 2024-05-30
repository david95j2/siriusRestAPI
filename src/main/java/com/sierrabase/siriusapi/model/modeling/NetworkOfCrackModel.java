package com.sierrabase.siriusapi.model.modeling;

import com.sierrabase.siriusapi.entity.modeling.NetworkOfCrackEntity;
import com.sierrabase.siriusapi.entity.modeling.ThreeDimensionalModelingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NetworkOfCrackModel {
    private int id;
    private int threeDimensionalModelId;
    private String networkOfCrackUrl;
    private String status;
    private ZonedDateTime createdDatetime;

    public NetworkOfCrackModel(final NetworkOfCrackEntity entity) {
        setId(entity.getNetwork_of_crack_id());
        setThreeDimensionalModelId(entity.getThree_dimensional_model_id());
        setNetworkOfCrackUrl(entity.getNetwork_of_crack_url());
        setStatus(entity.getStatus());
        setCreatedDatetime(entity.getCreated_datetime());
    }
}
