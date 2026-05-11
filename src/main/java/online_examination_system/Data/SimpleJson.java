package online_examination_system.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Small JSON parser/writer for the project's file storage.
 * Supports objects, arrays, strings, numbers, booleans and null.
 */
class SimpleJson {

    private final String text;
    private int index;

    private SimpleJson(String text) {
        this.text = text == null ? "" : text;
    }

    static Object parse(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new SimpleJson(text).parseValue();
    }

    static String stringify(Object value) {
        StringBuilder builder = new StringBuilder();
        writeValue(builder, value, 0);
        return builder.toString();
    }

    private Object parseValue() {
        skipWhitespace();
        if (index >= text.length()) {
            return null;
        }
        char c = text.charAt(index);
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return parseString();
        if (c == 't' || c == 'f') return parseBoolean();
        if (c == 'n') {
            index += 4;
            return null;
        }
        return parseNumber();
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> object = new LinkedHashMap<>();
        index++;
        skipWhitespace();
        while (index < text.length() && text.charAt(index) != '}') {
            String key = parseString();
            skipWhitespace();
            if (index < text.length() && text.charAt(index) == ':') index++;
            Object value = parseValue();
            object.put(key, value);
            skipWhitespace();
            if (index < text.length() && text.charAt(index) == ',') {
                index++;
                skipWhitespace();
            }
        }
        if (index < text.length() && text.charAt(index) == '}') index++;
        return object;
    }

    private List<Object> parseArray() {
        List<Object> array = new ArrayList<>();
        index++;
        skipWhitespace();
        while (index < text.length() && text.charAt(index) != ']') {
            array.add(parseValue());
            skipWhitespace();
            if (index < text.length() && text.charAt(index) == ',') {
                index++;
                skipWhitespace();
            }
        }
        if (index < text.length() && text.charAt(index) == ']') index++;
        return array;
    }

    private String parseString() {
        StringBuilder builder = new StringBuilder();
        if (index < text.length() && text.charAt(index) == '"') index++;
        while (index < text.length()) {
            char c = text.charAt(index++);
            if (c == '"') break;
            if (c == '\\' && index < text.length()) {
                char escaped = text.charAt(index++);
                switch (escaped) {
                    case '"': builder.append('"'); break;
                    case '\\': builder.append('\\'); break;
                    case '/': builder.append('/'); break;
                    case 'b': builder.append('\b'); break;
                    case 'f': builder.append('\f'); break;
                    case 'n': builder.append('\n'); break;
                    case 'r': builder.append('\r'); break;
                    case 't': builder.append('\t'); break;
                    case 'u':
                        if (index + 4 <= text.length()) {
                            builder.append((char) Integer.parseInt(text.substring(index, index + 4), 16));
                            index += 4;
                        }
                        break;
                    default: builder.append(escaped);
                }
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    private Boolean parseBoolean() {
        if (text.startsWith("true", index)) {
            index += 4;
            return Boolean.TRUE;
        }
        index += 5;
        return Boolean.FALSE;
    }

    private Number parseNumber() {
        int start = index;
        while (index < text.length() && "-0123456789.eE+".indexOf(text.charAt(index)) >= 0) {
            index++;
        }
        String number = text.substring(start, index);
        if (number.contains(".") || number.contains("e") || number.contains("E")) {
            return Double.parseDouble(number);
        }
        return Long.parseLong(number);
    }

    private void skipWhitespace() {
        while (index < text.length() && Character.isWhitespace(text.charAt(index))) {
            index++;
        }
    }

    @SuppressWarnings("unchecked")
    private static void writeValue(StringBuilder builder, Object value, int indent) {
        if (value == null) {
            builder.append("null");
        } else if (value instanceof String) {
            builder.append('"').append(escape((String) value)).append('"');
        } else if (value instanceof Number || value instanceof Boolean) {
            builder.append(value);
        } else if (value instanceof Map) {
            writeObject(builder, (Map<String, Object>) value, indent);
        } else if (value instanceof List) {
            writeArray(builder, (List<Object>) value, indent);
        } else {
            builder.append('"').append(escape(String.valueOf(value))).append('"');
        }
    }

    private static void writeObject(StringBuilder builder, Map<String, Object> object, int indent) {
        builder.append("{");
        if (!object.isEmpty()) builder.append("\n");
        int count = 0;
        for (Map.Entry<String, Object> entry : object.entrySet()) {
            indent(builder, indent + 2);
            builder.append('"').append(escape(entry.getKey())).append("\": ");
            writeValue(builder, entry.getValue(), indent + 2);
            if (++count < object.size()) builder.append(",");
            builder.append("\n");
        }
        if (!object.isEmpty()) indent(builder, indent);
        builder.append("}");
    }

    private static void writeArray(StringBuilder builder, List<Object> array, int indent) {
        builder.append("[");
        if (!array.isEmpty()) builder.append("\n");
        for (int i = 0; i < array.size(); i++) {
            indent(builder, indent + 2);
            writeValue(builder, array.get(i), indent + 2);
            if (i < array.size() - 1) builder.append(",");
            builder.append("\n");
        }
        if (!array.isEmpty()) indent(builder, indent);
        builder.append("]");
    }

    private static void indent(StringBuilder builder, int spaces) {
        for (int i = 0; i < spaces; i++) {
            builder.append(' ');
        }
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
