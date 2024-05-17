package com.sierrabase.siriusapi.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginParams {
    private String userId;
    private String userPw;

    public void parse(Map<String, String> params) {
        if (params.containsKey("userId"))
            setUserId(params.get("userId"));
        if (params.containsKey("userPw"))
            setUserPw(params.get("userPw"));
    }
}
