package utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * LocalDateTimeMapDeserializer class deserializes correctly Map of Map.Entry<LocalDateTime, LocalDateTime> and Boolean.
 * It represents deserialized date time interval.
 */
public class LocalDateTimeMapDeserializer extends JsonDeserializer<Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean>> {
    /**
     * Deserializes JSON content into a map of date ranges (represented as LocalDateTime pairs) mapped to Boolean values.
     *
     * @param jp    The JsonParser object used for reading JSON content.
     * @param context  The DeserializationContext object providing contextual information during deserialization.
     * @return A map where each key is a date range (start-end) and the value is a Boolean indicating a status.
     * @throws IOException If there's an issue reading from the JsonParser.
     */
    @Override
    public Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> deserialize(JsonParser jp, DeserializationContext context)
            throws IOException {
        Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> result = new HashMap<>();
        JsonNode node = jp.getCodec().readTree(jp);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String keyString = field.getKey();
            Boolean value = field.getValue().asBoolean();

            String[] dates = keyString.split(",");
            LocalDateTime start = LocalDateTime.parse(dates[0], formatter);
            LocalDateTime end = LocalDateTime.parse(dates[1], formatter);

            Map.Entry<LocalDateTime, LocalDateTime> key = new AbstractMap.SimpleEntry<>(start, end);
            result.put(key, value);
        }
        return result;
    }
}