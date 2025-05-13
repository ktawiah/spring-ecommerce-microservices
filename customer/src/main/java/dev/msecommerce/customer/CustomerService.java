package dev.msecommerce.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public String createCustomer(CustomerRequest request) {
        Customer savedCustomer = customerRepository.save(modelMapper.map(request, Customer.class));
        return savedCustomer.getId();
    }


    public Void updateCustomer(@Valid CustomerRequest request) {
        Customer customer = customerRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Customer with id: %s not found".formatted(request.getId())));
        customerRepository.save(customer);
        return null;
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream().map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    public void deleteCustomer(String id) {
        customerRepository.findById(id)
                .ifPresentOrElse(customerRepository::delete, () -> {
                    throw new RuntimeException("");
                });
    }

    public CustomerResponse getCustomerById(String id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceAccessException("Customer with id: %s not found".formatted(id)));
        return modelMapper.map(customer, CustomerResponse.class);
    }
}
