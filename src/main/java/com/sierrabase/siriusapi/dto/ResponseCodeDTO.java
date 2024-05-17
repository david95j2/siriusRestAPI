package com.sierrabase.siriusapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseCodeDTO<T> {
    private boolean success;
    private int status;
    private String message;
}
