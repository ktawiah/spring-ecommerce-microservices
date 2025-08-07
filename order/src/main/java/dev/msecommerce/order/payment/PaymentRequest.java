package dev.msecommerce.order.payment;

import dev.msecommerce.order.customer.CustomerResponse;
import dev.msecommerce.order.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
