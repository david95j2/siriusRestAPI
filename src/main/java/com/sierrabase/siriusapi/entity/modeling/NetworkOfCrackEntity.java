package com.sierrabase.siriusapi.entity.modeling;

import com.sierrabase.siriusapi.model.modeling.NetworkOfCrackModel;
import com.sierrabase.siriusapi.model.modeling.ThreeDimensionalModelingModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="network_of_crack")
public class NetworkOfCrackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "network_of_crack_id", unique = true, nullable = false)
    private int network_of_crack_id;
    @Column(name = "three_dimensional_model_id")
    private int three_dimensional_model_id;
    @Column(name = "network_of_crack_url")
    private String network_of_crack_url;
    @Column(name = "status")
    private String status;
    @Column(name = "created_datetime")
    private ZonedDateTime created_datetime;
    @Column(name = "wr_datetime")
    private ZonedDateTime wr_datetime;

    public NetworkOfCrackEntity(final NetworkOfCrackModel model) {
        setNetwork_of_crack_id(model.getId());
        setThree_dimensional_model_id(model.getThreeDimensionalModelId());
        setNetwork_of_crack_url(model.getNetworkOfCrackUrl());
        setStatus(model.getStatus());
        setCreated_datetime(model.getCreatedDatetime());
    }

    public NetworkOfCrackEntity(final Integer id, final NetworkOfCrackModel model) {
        this(model);
        setNetwork_of_crack_id(id);
    }
}
