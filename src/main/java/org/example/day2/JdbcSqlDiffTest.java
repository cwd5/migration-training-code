package org.example.day2;

import org.example.KeyStore;

import java.sql.*;
import java.util.List;

public class JdbcSqlDiffTest {

    public static void main(String[] args) throws Exception {

        // Oracle 连接
        Connection oracleConn = DriverManager.getConnection(
                "jdbc:oracle:thin:@//192.168.0.168:1521/xe",
                "system",
                new KeyStore().oracle_password
        );

        // Snowflake 连接
        Connection snowflakeConn = DriverManager.getConnection(
                "jdbc:snowflake://dqxoosj-oc93584.snowflakecomputing.com/?db=MIGRATION_TRAINING&schema=PRACTICE&warehouse=COMPUTE_WH",
                "AlkeneX",
                new KeyStore().snowflake_password
        );

        List<String> oracleSqls = List.of(
                "SELECT SYSDATE FROM DUAL",
                "SELECT SYSDATE + 7 FROM DUAL",
                "SELECT TO_DATE('2025-01-10','YYYY-MM-DD') - TO_DATE('2025-01-01','YYYY-MM-DD') FROM DUAL" ,
                "SELECT NVL(salary,0) FROM employees",
                "SELECT *\n" +
                        "FROM employees\n" +
                        "OFFSET 1 ROWS FETCH NEXT 1 ROWS ONLY\n"
        );

        List<String> snowflakeSqls = List.of(
                "SELECT CURRENT_DATE();",
                "SELECT DATEADD(day,7,CURRENT_DATE());",
                "SELECT DATEDIFF(\n" +
                        "  day,\n" +
                        "  TO_DATE('2025-01-01'),\n" +
                        "  TO_DATE('2025-01-10')\n" +
                        ");\n",
                "SELECT COALESCE(salary,0) FROM employees;",
                "SELECT *\n" +
                        "FROM employees\n" +
                        "LIMIT 1 OFFSET 1;\n"

        );

        System.out.println("====== Oracle SQL 测试 ======");
        executeSqls(oracleConn, oracleSqls);

        System.out.println("====== Snowflake SQL 测试 ======");
        executeSqls(snowflakeConn, snowflakeSqls);

        oracleConn.close();
        snowflakeConn.close();
    }

    private static void executeSqls(Connection conn, List<String> sqls) {
        for (String sql : sqls) {
            System.out.println("执行 SQL: " + sql);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                while (rs.next()) {
                    for (int i = 1; i <= colCount; i++) {
                        System.out.print(rs.getObject(i) + "\t");
                    }
                    System.out.println();
                }

            } catch (SQLException e) {
                System.err.println("SQL 执行失败: " + e.getMessage());
            }
        }
    }
}
