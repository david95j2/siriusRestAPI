package com.sierrabase.siriusapi.common;

public class URIParser {
    static public int parseStringToIntegerId(String id) {
        int res = -1;
        try {
            res = Integer.parseInt(id);
        } catch (Exception e) {
            res = -1;
        }

        return res;
    }
}
