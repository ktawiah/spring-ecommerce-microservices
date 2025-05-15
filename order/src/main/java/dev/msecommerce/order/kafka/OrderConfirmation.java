package dev.msecommerce.order.kafka;


import dev.msecommerce.order.customer.CustomerResponse;
import dev.msecommerce.order.order.PaymentMethod;
import dev.msecommerce.order.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}