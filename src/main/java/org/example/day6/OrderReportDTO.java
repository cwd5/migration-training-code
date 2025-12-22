package org.example.day6;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderReportDTO {

    private long userId;
    private String userName;
    private int orderCount;
    private BigDecimal totalAmount;

    public OrderReportDTO(long userId,
                          String userName,
                          int orderCount,
                          BigDecimal totalAmount) {
        this.userId = userId;
        this.userName = userName;
        this.orderCount = orderCount;
        this.totalAmount = totalAmount;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    // BigDecimal 用 compareTo，避免精度问题
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderReportDTO)) return false;
        OrderReportDTO that = (OrderReportDTO) o;
        return userId == that.userId &&
                orderCount == that.orderCount &&
                Objects.equals(userName, that.userName) &&
                totalAmount.compareTo(that.totalAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, orderCount,
                totalAmount.stripTrailingZeros());
    }
}
