package org.example.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderSummary(
        Long orderId,
        String customerName,
        String status,
        BigDecimal total,
        List<Line> lines
) {
    public record Line(String product, int quantity, BigDecimal unitPrice) {}
}