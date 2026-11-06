package sg.edu.ntu.simple_crm.mapper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import sg.edu.ntu.simple_crm.dto.CustomerRequest;
import sg.edu.ntu.simple_crm.dto.CustomerResponse;
import sg.edu.ntu.simple_crm.model.Customer;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequest request) {
        if (request == null) {
            return null;
        }

        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .company(request.getCompany())
                .status(request.getStatus() == null ? "active" : request.getStatus())
                .tags(request.getTags() == null ? new ArrayList<>() : new ArrayList<>(request.getTags()))
                .notes(request.getNotes())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .company(customer.getCompany())
                .status(customer.getStatus())
                .tags(customer.getTags() == null ? List.of() : List.copyOf(customer.getTags()))
                .notes(customer.getNotes())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
