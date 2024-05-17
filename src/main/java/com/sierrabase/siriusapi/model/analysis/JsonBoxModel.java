package com.sierrabase.siriusapi.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonBoxModel {
    private Double xmin;
    private Double ymin;
    private Double xmax;
    private Double ymax;
}

