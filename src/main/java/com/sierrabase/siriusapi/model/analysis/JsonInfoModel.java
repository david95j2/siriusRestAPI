package com.sierrabase.siriusapi.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonInfoModel {
    private Integer mask_id;
    private Double crack_width;
    private Double crack_length;
    private JsonBoxModel box;
}

