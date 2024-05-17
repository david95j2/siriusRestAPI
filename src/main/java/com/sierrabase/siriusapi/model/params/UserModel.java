package com.sierrabase.siriusapi.model.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
// RegisterUserParam --> UserModel
public class UserModel {
    private String userId;
    private String userPw;

}
