package org.example.day5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OracleToSnowflakeConverter {

    public static String convert(String oracleDDL) {
        String ddl = oracleDDL.toUpperCase();

        // 1. 数据类型映射
        ddl = ddl.replaceAll("VARCHAR2", "VARCHAR");
        ddl = ddl.replaceAll("NUMBER\\s*\\((\\d+),(\\d+)\\)", "NUMBER($1,$2)");
        ddl = ddl.replaceAll("NUMBER\\s*\\((\\d+)\\)", "NUMBER($1)");
        ddl = ddl.replaceAll("\\bNUMBER\\b", "NUMBER");
        ddl = ddl.replaceAll("\\bDATE\\b", "TIMESTAMP_NTZ");
        ddl = ddl.replaceAll("\\bTIMESTAMP\\b", "TIMESTAMP_NTZ");
        ddl = ddl.replaceAll("\\bCLOB\\b", "STRING");
        ddl = ddl.replaceAll("\\bBLOB\\b", "BINARY");

        // 2. 移除 Oracle 特有语法
        ddl = removeClause(ddl, "TABLESPACE");
        ddl = removeClause(ddl, "STORAGE");
        ddl = removeClause(ddl, "SEGMENT CREATION");

        ddl = ddl.replaceAll("ENABLE", "");
        ddl = ddl.replaceAll("NOT NULL\\s+", "NOT NULL ");
        // 3. 清理多余空格
        ddl = ddl.replaceAll("\\s+", " ");
        ddl = ddl.replaceAll(", ", ",\n  ");

        return ddl.trim();
    }

    private static String removeClause(String ddl, String keyword) {
        Pattern pattern = Pattern.compile(keyword + ".*?(\\)|;)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ddl);
        return matcher.replaceAll("$1");
    }

    // 示例测试

}
