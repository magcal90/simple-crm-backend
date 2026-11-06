package sg.edu.ntu.simple_crm.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import sg.edu.ntu.simple_crm.dto.CustomerRequest;
import sg.edu.ntu.simple_crm.dto.CustomerResponse;
import sg.edu.ntu.simple_crm.model.Customer;

class CustomerMapperTest {

    private final CustomerMapper mapper = new CustomerMapper();

    @Test
    void mapsFrontendCustomerContractInBothDirections() {
        CustomerRequest request = CustomerRequest.builder()
                .firstName("Sarah")
                .lastName("Chen")
                .email("sarah@example.com")
                .phone("+65 9123 4567")
                .company("Fintech Pte Ltd")
                .status("active")
                .tags(List.of("VIP", "Referral"))
                .notes("Enterprise customer")
                .build();

        Customer entity = mapper.toEntity(request);

        assertEquals(request.getPhone(), entity.getPhone());
        assertEquals(request.getCompany(), entity.getCompany());
        assertEquals(request.getStatus(), entity.getStatus());
        assertEquals(request.getTags(), entity.getTags());
        assertEquals(request.getNotes(), entity.getNotes());

        entity.setId(1L);
        entity.setCreatedAt(LocalDate.of(2026, 11, 2));
        CustomerResponse response = mapper.toResponse(entity);

        assertEquals(1L, response.getId());
        assertEquals(request.getPhone(), response.getPhone());
        assertEquals(request.getCompany(), response.getCompany());
        assertEquals(request.getStatus(), response.getStatus());
        assertEquals(request.getTags(), response.getTags());
        assertEquals(request.getNotes(), response.getNotes());
        assertEquals(LocalDate.of(2026, 11, 2), response.getCreatedAt());
    }

    @Test
    void suppliesFrontendSafeDefaults() {
        Customer entity = mapper.toEntity(CustomerRequest.builder()
                .firstName("Sarah")
                .lastName("Chen")
                .email("sarah@example.com")
                .build());

        assertEquals("active", entity.getStatus());
        assertTrue(entity.getTags().isEmpty());
    }
}
