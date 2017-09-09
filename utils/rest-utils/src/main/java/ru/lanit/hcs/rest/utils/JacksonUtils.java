package ru.lanit.hcs.rest.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.NullNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JacksonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Integer asInt(String json, String keyName) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return tree.findValue(keyName).asInt();
        } catch (IOException ex) {
            return null;
        }
    }

    public static Boolean asBoolean(String json, String keyName) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return tree.findValue(keyName).asBoolean();
        } catch (IOException ex) {
            return null;
        }
    }

    public static Long asLong(String json, String keyName) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return tree.findValue(keyName).asLong();
        } catch (IOException ex) {
            return null;
        }
    }

    public static List<String> asList(String json, String keyName) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            JsonNode arrayNode = tree.get(keyName);
            List<String> result = new ArrayList<String>();
            if (!arrayNode.isArray()) {
                throw new IllegalStateException("Your property is not of array type. Use another method instead.");
            }
            for (JsonNode node: arrayNode) {
                result.add(node.asText());
            }
            return result;
        } catch (IOException ex) {
            return Collections.emptyList();
        }
    }

    public static String asText(String json, String keyName) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return tree.findValue(keyName).asText();
        } catch (IOException ex) {
            return StringUtils.EMPTY;
        }
    }

    public static Double asDouble(String json, String keyName) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return tree.findValue(keyName).asDouble();
        } catch (IOException ex) {
            return null;
        }
    }

    public JsonNode asChildNode(String json, String keyName) {
        try {
            JsonNode tree = objectMapper.readTree(json);
            return tree.findValue(keyName);
        } catch (IOException ex) {
            return NullNode.getInstance();
        }
    }
}
