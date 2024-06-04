package com.sierrabase.siriusapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SourceInfoModel {
    private String url;
    private Integer albumId;
    private boolean isTopFolder;
    private boolean isExistOrigin;
    private boolean isExistAnalysis;
}
