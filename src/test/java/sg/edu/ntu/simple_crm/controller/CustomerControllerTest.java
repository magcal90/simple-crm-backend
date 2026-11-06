package sg.edu.ntu.simple_crm.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import sg.edu.ntu.simple_crm.model.Customer;
import sg.edu.ntu.simple_crm.repository.CustomerRepository;
import sg.edu.ntu.simple_crm.security.JwtService;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerRepository customerRepository;

    private String authorization;

    @BeforeEach
    void authenticate() {
        authorization = "Bearer " + jwtService.generateToken("controller-test@example.com");
    }

    @Test
    public void getCustomerById_existingId_returnOk() throws Exception {
        Customer customer = customerRepository.save(Customer.builder()
                .firstName("Test").lastName("Customer")
                .email("controller-test@example.com")
                .status("active")
                .build());
        RequestBuilder request = MockMvcRequestBuilders.get("/customers/{id}", customer.getId())
                .header(HttpHeaders.AUTHORIZATION, authorization);

        // Step 2: Perform and assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customer.getId()));

    }

    @Test
    public void createCustomer_validCustomer_returnsCreated() throws Exception {
        // Step 1:
        // create a Customer objectMapper

        Customer newCustomer = Customer.builder()
                .firstName("Clint").lastName("Barton")
                .email("clint@avengers.com").phone("12345678")
                .company("Avengers").status("active")
                .tags(java.util.List.of("VIP")).notes("Special Agent")
                .build();

        // Step 2: Convert java object to a JSON formatted String:
        String newCustomerAsJSON = objectMapper.writeValueAsString(newCustomer)  ;
        
        // Step 3: Build the POST request
        RequestBuilder request = MockMvcRequestBuilders.post("/customers")
        .header(HttpHeaders.AUTHORIZATION, authorization)
        .contentType(MediaType.APPLICATION_JSON)
        .content(newCustomerAsJSON);

        //Step 4: Perform and Assert
        mockMvc.perform(request)
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.firstName").value("Clint"))
        .andExpect(jsonPath("$.lastName").value("Barton"))
        .andExpect(jsonPath("$.phone").value("12345678"))
        .andExpect(jsonPath("$.company").value("Avengers"))
        .andExpect(jsonPath("$.status").value("active"))
        .andExpect(jsonPath("$.tags[0]").value("VIP"))
        .andExpect(jsonPath("$.notes").value("Special Agent"))
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.contactNo").doesNotExist())
        .andExpect(jsonPath("$.jobTitle").doesNotExist())
        .andExpect(jsonPath("$.yearOfBirth").doesNotExist());

    }

    @Test
    public void getCustomerById_withoutToken_returnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/customers/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createCustomer_withoutToken_returnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Clint",
                                  "lastName": "Barton",
                                  "email": "clint@avengers.com"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

}
