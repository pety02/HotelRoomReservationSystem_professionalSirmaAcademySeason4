package utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;

public class LocalDateTimeMapEntryDeserializer extends JsonDeserializer<Map.Entry<LocalDateTime, LocalDateTime>> {
    @Override
    public Map.Entry<LocalDateTime, LocalDateTime> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime key = LocalDateTime.parse(node.get("key").asText(), formatter);
        LocalDateTime value = LocalDateTime.parse(node.get("value").asText(), formatter);

        return new AbstractMap.SimpleEntry<>(key, value);
    }
}