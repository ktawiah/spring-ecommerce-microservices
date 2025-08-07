package dev.msecommerce.order.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentClient {

    @Value("${application.config.payment-url}")
    private String paymentUrl;

    private final RestTemplate restTemplate;

    public void requestOrderPayment(PaymentRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);
            HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<Void> response = restTemplate.postForEntity(paymentUrl, entity, Void.class);
            if (response.getStatusCode().isError()) {
                log.warn("Payment request returned status: {}", response.getStatusCode());
            }
        } catch (RestClientException ex) {
            log.warn("Payment service not reachable or error occurred: {}", ex.getMessage());
        }
    }
}
