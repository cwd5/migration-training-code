package org.example.day3;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OracleToSnowflakeTypeMapper {

    // NUMBER(p,s)
    private static final Pattern NUMBER_PATTERN =
            Pattern.compile("NUMBER\\s*\\((\\d+)(?:\\s*,\\s*(\\d+))?\\)", Pattern.CASE_INSENSITIVE);

    // CHAR(n) / VARCHAR2(n) / NVARCHAR2(n)
    private static final Pattern CHAR_PATTERN =
            Pattern.compile("(CHAR|VARCHAR2|NVARCHAR2)\\s*\\((\\d+)\\)", Pattern.CASE_INSENSITIVE);

    /**
     * 核心映射方法
     */
    public static String map(String oracleType) {
        if (oracleType == null || oracleType.trim().isEmpty()) {
            return null;
        }

        String type = oracleType.trim().toUpperCase(Locale.ROOT);

        // 1. NUMBER(p,s)
        Matcher numberMatcher = NUMBER_PATTERN.matcher(type);
        if (numberMatcher.matches()) {
            String precision = numberMatcher.group(1);
            String scale = numberMatcher.group(2);
            if (scale == null) {
                return "NUMBER(" + precision + ",0)";
            }
            return "NUMBER(" + precision + "," + scale + ")";
        }

        // 2. NUMBER (无精度)
        if (type.equals("NUMBER")) {
            return "NUMBER(38,0)";
        }

        // 3. FLOAT / BINARY_FLOAT / BINARY_DOUBLE
        if (type.equals("FLOAT") ||
                type.equals("BINARY_FLOAT") ||
                type.equals("BINARY_DOUBLE")) {
            return "FLOAT";
        }

        // 4. CHAR / VARCHAR2 / NVARCHAR2
        Matcher charMatcher = CHAR_PATTERN.matcher(type);
        if (charMatcher.matches()) {
            String length = charMatcher.group(2);
            return "VARCHAR(" + length + ")";
        }

        if (type.equals("CHAR") ||
                type.equals("VARCHAR2") ||
                type.equals("NVARCHAR2")) {
            return "VARCHAR";
        }

        // 5. DATE
        if (type.equals("DATE")) {
            return "TIMESTAMP_NTZ";
        }

        // 6. TIMESTAMP
        if (type.startsWith("TIMESTAMP")) {
            if (type.contains("WITH TIME ZONE")) {
                return "TIMESTAMP_TZ";
            }
            if (type.contains("WITH LOCAL TIME ZONE")) {
                return "TIMESTAMP_LTZ";
            }
            return "TIMESTAMP_NTZ";
        }

        // 7. LOB
        if (type.equals("CLOB") || type.equals("NCLOB")) {
            return "VARCHAR";
        }
        if (type.equals("BLOB")) {
            return "BINARY";
        }

        // 8. RAW
        if (type.startsWith("RAW")) {
            return "BINARY";
        }

        // 9. JSON / XML
        if (type.equals("JSON")) {
            return "VARIANT";
        }
        if (type.equals("XMLTYPE")) {
            return "VARIANT";
        }

        // 10. BOOLEAN
        if (type.equals("BOOLEAN")) {
            return "BOOLEAN";
        }

        // 兜底策略
        return "VARCHAR";
    }

    // ================== 测试示例 ==================
    public static void main(String[] args) {
        String[] oracleTypes = {
                "NUMBER",
                "NUMBER(10)",
                "NUMBER(12,2)",
                "VARCHAR2(100)",
                "CHAR(10)",
                "DATE",
                "TIMESTAMP",
                "TIMESTAMP WITH TIME ZONE",
                "CLOB",
                "BLOB",
                "JSON"
        };

        for (String oracleType : oracleTypes) {
            System.out.printf("%-30s -> %s%n",
                    oracleType, map(oracleType));
        }
    }
}
