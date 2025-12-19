import org.example.day5.OracleToSnowflakeConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class OracleToSnowflakeConverterTest {

    @Test
    void testBasicTypeConversion() {
        String oracleDDL = """
            CREATE TABLE TEST1 (
                ID NUMBER(10),
                NAME VARCHAR2(100),
                CREATED_DATE DATE
            );
            """;

        String snowflakeDDL = OracleToSnowflakeConverter.convert(oracleDDL);

        assertTrue(snowflakeDDL.contains("ID NUMBER(10)"));
        assertTrue(snowflakeDDL.contains("NAME VARCHAR(100)"));
        assertTrue(snowflakeDDL.contains("CREATED_DATE TIMESTAMP_NTZ"));
    }

    @Test
    void testNumberPrecisionScaleConversion() {
        String oracleDDL = """
            CREATE TABLE TEST2 (
                SALARY NUMBER(10,2)
            );
            """;

        String snowflakeDDL = OracleToSnowflakeConverter.convert(oracleDDL);
        System.out.println(snowflakeDDL);
        assertTrue(snowflakeDDL.contains("SALARY NUMBER(10,2)"));
    }

    @Test
    void testNotNullEnableConversion() {
        String oracleDDL = """
            CREATE TABLE TEST3 (
                ID NUMBER NOT NULL ENABLE
            );
            """;

        String snowflakeDDL = OracleToSnowflakeConverter.convert(oracleDDL);

        assertTrue(snowflakeDDL.contains("NOT NULL"));
        assertFalse(snowflakeDDL.contains("ENABLE"));
    }

    @Test
    void testTablespaceRemoval() {
        String oracleDDL = """
            CREATE TABLE TEST4 (
                ID NUMBER
            ) TABLESPACE USERS;
            """;

        String snowflakeDDL = OracleToSnowflakeConverter.convert(oracleDDL);

        assertFalse(snowflakeDDL.contains("TABLESPACE"));
        assertFalse(snowflakeDDL.contains("USERS"));
    }

    @Test
    void testPrimaryKeyConstraintPreserved() {
        String oracleDDL = """
            CREATE TABLE TEST5 (
                ID NUMBER,
                CONSTRAINT TEST5_PK PRIMARY KEY (ID) ENABLE
            );
            """;

        String snowflakeDDL = OracleToSnowflakeConverter.convert(oracleDDL);

        assertTrue(snowflakeDDL.contains("PRIMARY KEY (ID)"));
        assertFalse(snowflakeDDL.contains("ENABLE"));
    }

    @Test
    void testClobAndBlobConversion() {
        String oracleDDL = """
            CREATE TABLE TEST6 (
                DOC CLOB,
                BIN_DATA BLOB
            );
            """;

        String snowflakeDDL = OracleToSnowflakeConverter.convert(oracleDDL);

        assertTrue(snowflakeDDL.contains("DOC STRING"));
        assertTrue(snowflakeDDL.contains("BIN_DATA BINARY"));
    }
}
