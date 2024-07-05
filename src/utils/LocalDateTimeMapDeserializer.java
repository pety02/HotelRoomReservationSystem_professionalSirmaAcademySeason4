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
 *
 */
public class LocalDateTimeMapDeserializer extends JsonDeserializer<Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean>> {
    /**
     *
     * @param jp
     * @param ctxt
     * @return
     * @throws IOException
     */
    @Override
    public Map<Map.Entry<LocalDateTime, LocalDateTime>, Boolean> deserialize(JsonParser jp, DeserializationContext ctxt)
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