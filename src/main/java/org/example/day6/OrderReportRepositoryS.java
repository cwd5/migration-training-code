package org.example.day6;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OrderReportRepositoryS {

    private static final String REPORT_SQL =
            "SELECT u.user_id, u.user_name, " +
                    "       COUNT(o.order_id) AS order_count, " +
                    "       SUM(oi.quantity * oi.price) AS total_amount " +
                    "FROM users u " +
                    "JOIN orders o ON u.user_id = o.user_id " +
                    "JOIN order_items oi ON o.order_id = oi.order_id " +
                    "WHERE o.order_date >= ? " +
                    "GROUP BY u.user_id, u.user_name " +
                    "ORDER BY u.user_id";

    public List<OrderReportDTO> queryOrderReport(Connection conn)
            throws Exception {

        List<OrderReportDTO> result = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(REPORT_SQL)) {

            ps.setDate(1, Date.valueOf("2024-01-01"));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(
                            new OrderReportDTO(
                                    rs.getLong("USER_ID"),
                                    rs.getString("USER_NAME"),
                                    rs.getInt("ORDER_COUNT"),
                                    rs.getBigDecimal("TOTAL_AMOUNT")
                            )
                    );
                }
            }
        }
        return result;
    }
}
