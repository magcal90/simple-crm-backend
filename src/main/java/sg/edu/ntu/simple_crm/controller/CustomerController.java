package sg.edu.ntu.simple_crm.controller;

import java.util.List;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import sg.edu.ntu.simple_crm.dto.CustomerRequest;
import sg.edu.ntu.simple_crm.dto.CustomerResponse;
import sg.edu.ntu.simple_crm.dto.InteractionRequest;
import sg.edu.ntu.simple_crm.dto.InteractionResponse;
import sg.edu.ntu.simple_crm.mapper.CustomerMapper;
import sg.edu.ntu.simple_crm.mapper.InteractionMapper;
import sg.edu.ntu.simple_crm.model.Customer;
import sg.edu.ntu.simple_crm.model.Interaction;
import sg.edu.ntu.simple_crm.service.CustomerServiceImpl;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;
    private final CustomerMapper customerMapper;
    private final InteractionMapper interactionMapper;

    public CustomerController(CustomerServiceImpl customerService, CustomerMapper customerMapper,
            InteractionMapper interactionMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        this.interactionMapper = interactionMapper;
    }

    // Create
    @PostMapping()
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) {

        Customer newCustomer = customerService.createCustomer(customerMapper.toEntity(request));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCustomer.getId())
                .toUri();
        return ResponseEntity.created(location).body(customerMapper.toResponse(newCustomer));
    }

    // Read (Get All)
    @GetMapping()
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        return ResponseEntity.ok(allCustomers.stream().map(customerMapper::toResponse).toList());
    }

    // get customer by id
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {

        Customer foundCustomer = customerService.getCustomer(id);
        return ResponseEntity.ok(customerMapper.toResponse(foundCustomer));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponse>> searchCustomers(@RequestParam String firstName) {
        List<Customer> foundCustomers = customerService.searchCustomers(firstName);
        return ResponseEntity.ok(foundCustomers.stream().map(customerMapper::toResponse).toList());

    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
            @RequestBody @Valid CustomerRequest request) {

        Customer updatedCustomer = customerService.updateCustomer(id, customerMapper.toEntity(request));
        return ResponseEntity.ok(customerMapper.toResponse(updatedCustomer));

    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{id}/interactions")
    public ResponseEntity<InteractionResponse> addInteractionToCustomer(@PathVariable Long id,
            @RequestBody @Valid InteractionRequest request) {

        Interaction newInteraction = customerService.addInteractionToCustomer(id, interactionMapper.toEntity(request));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newInteraction.getId())
                .toUri();

        return ResponseEntity.created(location).body(interactionMapper.toResponse(newInteraction));
    }

    @GetMapping("/{id}/interactions")
    public ResponseEntity<List<InteractionResponse>> getInteractionToCustomer(@PathVariable Long id) {

        return ResponseEntity.ok(customerService.findInteractionsByCustomerId(id).stream()
                .map(interactionMapper::toResponse)
                .toList());

    }

    @GetMapping("/{customerId}/interactions/{interactionId}")
    public ResponseEntity<InteractionResponse> getStepById(@PathVariable Long customerId,
            @PathVariable Long interactionId) {

        return ResponseEntity.ok(interactionMapper.toResponse(
                customerService.findInteractionById(customerId, interactionId)));
    }

}
