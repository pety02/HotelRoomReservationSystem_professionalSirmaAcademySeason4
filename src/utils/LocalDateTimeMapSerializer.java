package utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;

public class LocalDateTimeMapSerializer extends JsonSerializer<Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean>> {

    @Override
    public void serialize(Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> map, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        for (Map.Entry<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> entry : map.entrySet()) {
            Map.Entry<LocalDateTime, LocalDateTime> key = entry.getKey();
            Boolean value = entry.getValue();

            String keyString = key.getKey().toString() + "," + key.getValue().toString();
            gen.writeBooleanField(keyString, value);
        }
        gen.writeEndObject();
    }
}