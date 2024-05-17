package com.sierrabase.siriusapi.service.json;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ZonedDateTimeSerializer implements JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {
    @Override
    public JsonElement serialize(
        ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_INSTANT));
        //return new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }

    @Override
    public ZonedDateTime deserialize(
            JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        return ZonedDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
