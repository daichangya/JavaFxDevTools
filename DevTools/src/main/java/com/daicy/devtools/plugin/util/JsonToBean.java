package com.daicy.devtools.plugin.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JSON 转 JavaBean 工具类
 * 
 * <p>提供将 JSON 对象转换为 JavaBean 代码的功能。
 * 
 * @author daicy
 */
public class JsonToBean {
    public static String generateJavaBeanCode(ObjectNode jsonObject) {
        StringBuilder javaBeanCode = new StringBuilder();
        String className = getClassNameFromJson(jsonObject);
        javaBeanCode.append("public class ").append(className).append(" {\n");

        List<String> fields = new ArrayList<>();
        jsonObject.fields().forEachRemaining(entry -> {
            String fieldName = toCamelCase(entry.getKey());
            String fieldType = getJavaTypeFromJson(entry.getValue());
            fields.add("    private " + fieldType + " " + fieldName + ";\n");
            javaBeanCode.append("    public ").append(fieldType).append(" get").append(capitalize(fieldName)).append("() {\n");
            javaBeanCode.append("        return ").append(fieldName).append(";\n");
            javaBeanCode.append("    }\n");
            javaBeanCode.append("    public void set").append(capitalize(fieldName)).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
            javaBeanCode.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
            javaBeanCode.append("    }\n");
        });

        javaBeanCode.append("\n");
        fields.forEach(javaBeanCode::append);
        javaBeanCode.append("}\n");

        return javaBeanCode.toString();
    }

    private static String getClassNameFromJson(ObjectNode jsonObject) {
        String firstKey = jsonObject.fieldNames().next();
        return toCamelCase(firstKey) + "Bean";
    }

    private static  String getJavaTypeFromJson(JsonNode jsonNode) {
        if (jsonNode.isTextual()) {
            return "String";
        } else if (jsonNode.isBoolean()) {
            return "boolean";
        } else if (jsonNode.isInt()) {
            return "int";
        } else if (jsonNode.isLong()) {
            return "long";
        } else if (jsonNode.isDouble()) {
            return "double";
        } else if (jsonNode.isObject()) {
            return getClassNameFromJson((ObjectNode) jsonNode);
        } else if (jsonNode.isArray()) {
            JsonNode firstElement = jsonNode.elements().next();
            return "List<" + getJavaTypeFromJson(firstElement) + ">";
        }
        return "Object";
    }

    private static String toCamelCase(String input) {
        return input;
    }

    private static String capitalize(String input) {
        if (input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
