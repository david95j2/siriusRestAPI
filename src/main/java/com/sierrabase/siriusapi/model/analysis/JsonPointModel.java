package com.sierrabase.siriusapi.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonPointModel {
    private Integer mask_id;
    private ArrayList<ArrayList<Integer>> points;
}

