package com.sierrabase.siriusapi.model.inspection;

import com.sierrabase.siriusapi.entity.inspection.IMPWpsMissionCameraEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class FitAreaProgramModel {
    private String userId;
    private String port;
}
