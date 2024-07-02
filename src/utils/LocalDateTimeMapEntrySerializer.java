package utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class LocalDateTimeMapEntrySerializer extends JsonSerializer<Map.Entry<LocalDateTime, LocalDateTime>> {
    @Override
    public void serialize(Map.Entry<LocalDateTime, LocalDateTime> entry, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("key", entry.getKey().toString());
        gen.writeStringField("value", entry.getValue().toString());
        gen.writeEndObject();
    }
}