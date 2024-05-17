package com.sierrabase.siriusapi.service.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Slf4j
@Service
public class JSONCommunicator {
    private static final Logger logger = LoggerFactory.getLogger(JSONCommunicator.class);

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String DATE_FORMAT_WITH_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final int DATE_FORMAT_WITH_MS_LENGTH = 24;

    private static Gson gson;
    private static GsonBuilder gsonBuilder;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeSerializer());
        //builder.registerTypeAdapter(ZonedDateTime.class, new JsonDateTimeAdapter());
        builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }

            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getAnnotation(Exclude.class) != null;
            }
        });

        gson = builder.disableHtmlEscaping().setLenient().create();
    }

    public <T> T unpackPayload(Object payload, Class<T> type) throws Exception {
        return gson.fromJson(payload.toString(), type);
    }

    public Object packPayload(Object payload) {
        return gson.toJson(payload);
    }
//
//    public Object makeCallResult(String uniqueId, String action, Object payload) {
//        return String.format(CALLRESULT_FORMAT, uniqueId, payload);
//    }
//
//    public Object makeCall(String uniqueId, String action, Object payload) {
//        return String.format(CALL_FORMAT, uniqueId, action, payload);
//    }
//
//    public Object makeCallError(
//            String uniqueId, String action, String errorCode, String errorDescription) {
//        return String.format(CALLERROR_FORMAT, uniqueId, errorCode, errorDescription, "{}");
//    }
}
