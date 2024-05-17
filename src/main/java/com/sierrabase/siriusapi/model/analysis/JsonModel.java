package com.sierrabase.siriusapi.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonModel {
    private ArrayList<JsonInfoModel> info;
    private ArrayList<JsonPointModel> points;
}

