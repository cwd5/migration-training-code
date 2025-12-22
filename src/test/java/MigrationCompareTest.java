import org.example.KeyStore;
import org.example.day6.OrderReportDTO;
import org.example.day6.OrderReportRepository;
import org.example.day6.UserDTO;
import org.example.day6.UserRepository;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MigrationCompareTest {

    private Connection oracleConn;
    private Connection snowflakeConn;

    private UserRepository userRepo;
    private OrderReportRepository reportRepo;

    @BeforeAll
    void init() throws Exception {

        oracleConn = DriverManager.getConnection(
                "jdbc:oracle:thin:@//192.168.0.169:1522/xe",
                "system",
                new KeyStore().oracle_password
        );

        snowflakeConn = DriverManager.getConnection(
                "jdbc:snowflake://dqxoosj-oc93584.snowflakecomputing.com/"
                        + "?db=MIGRATION_TRAINING"
                        + "&schema=PRACTICE"
                        + "&warehouse=COMPUTE_WH",
                "AlkeneX",
                new KeyStore().snowflake_password
        );

        userRepo = new UserRepository();
        reportRepo = new OrderReportRepository();
    }

    @AfterAll
    void close() throws Exception {
        oracleConn.close();
        snowflakeConn.close();
    }


    @Test
    @DisplayName("Oracle vs Snowflake - 用户数据一致性校验")
    void compareUsers() throws Exception {

        List<UserDTO> oracleUsers =
                userRepo.queryUsers(oracleConn);
        List<UserDTO> snowflakeUsers =
                userRepo.queryUsers(snowflakeConn);

        Assertions.assertEquals(
                oracleUsers.size(),
                snowflakeUsers.size(),
                "用户数量不一致"
        );

        Assertions.assertIterableEquals(
                oracleUsers,
                snowflakeUsers,
                "用户数据不一致"
        );
    }



    @Test
    @DisplayName("Oracle vs Snowflake - 订单报表一致性校验")
    void compareOrderReport() throws Exception {

        List<OrderReportDTO> oracleReport =
                reportRepo.queryOrderReport(oracleConn);
        List<OrderReportDTO> snowflakeReport =
                reportRepo.queryOrderReport(snowflakeConn);

        Assertions.assertEquals(
                oracleReport.size(),
                snowflakeReport.size(),
                "报表行数不一致"
        );

        Assertions.assertIterableEquals(
                oracleReport,
                snowflakeReport,
                "报表数据不一致"
        );
    }
}
