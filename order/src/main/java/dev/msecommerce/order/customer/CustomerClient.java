package dev.msecommerce.order.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerClient {

    @Value("${application.config.customer-url}")
    private String customerUrl;

    private final RestTemplate restTemplate;

    public Optional<CustomerResponse> findCustomerById(String id) {
        try {
            ResponseEntity<CustomerResponse> response = restTemplate.getForEntity(customerUrl + "/" + id, CustomerResponse.class);
            return Optional.ofNullable(response.getBody());
        } catch (RestClientException ex) {
            return Optional.empty();
        }
    }
}
