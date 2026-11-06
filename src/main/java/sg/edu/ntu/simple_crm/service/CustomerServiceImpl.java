package sg.edu.ntu.simple_crm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sg.edu.ntu.simple_crm.exceptions.CustomerNotFoundException;
import sg.edu.ntu.simple_crm.model.Customer;
import sg.edu.ntu.simple_crm.model.Interaction;
import sg.edu.ntu.simple_crm.repository.CustomerRepository;
import sg.edu.ntu.simple_crm.repository.InteractionRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final InteractionRepository interactionRepository;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, InteractionRepository interactionRepository) {
        this.customerRepository = customerRepository;
        this.interactionRepository = interactionRepository;
    }

    // Create
    @Override
    public Customer createCustomer(Customer customer) {
        Customer createdCustomer = customerRepository.save(customer);
        logger.info("Created customer with id {}", createdCustomer.getId());
        return createdCustomer;
    }

    // Get one customer
    @Override
    public Customer getCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        logger.info("Retrieved customer with id {}", id);
        return customer;
    }

    // Get All Customer
    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        logger.info("Retrieved {} customers", customers.size());
        return customers;
    }

    // updateCustomer
    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        // Retrieve customer to be updated
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // Update the fileds
        customerToUpdate.setFirstName(customer.getFirstName());
        customerToUpdate.setLastName(customer.getLastName());
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setPhone(customer.getPhone());
        customerToUpdate.setCompany(customer.getCompany());
        customerToUpdate.setStatus(customer.getStatus());
        customerToUpdate.setTags(customer.getTags());
        customerToUpdate.setNotes(customer.getNotes());

        // save and return the updated customer
        Customer updatedCustomer = customerRepository.save(customerToUpdate);
        logger.info("Updated customer with id {}", id);
        return updatedCustomer;
    }

    // delete
    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        logger.info("Deleted customer with id {}", id);
    }

    @Override
    public Interaction addInteractionToCustomer(Long id, Interaction interaction) {

        // Step 1: find the customer
        Customer selectedCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // Step 2: Link the customer to interaction
        interaction.setCustomer(selectedCustomer);

        // Step 3: Save and return the interaction
        Interaction savedInteraction = interactionRepository.save(interaction);
        logger.info("Added interaction with id {} to customer with id {}", savedInteraction.getId(), id);
        return savedInteraction;
    }

    @Override
    public List<Customer> searchCustomers(String firstName) {
        return customerRepository.findByFirstName(firstName);
    }

    @Override
    public List<Interaction> findInteractionsByCustomerId(Long id){
        List<Interaction> interactions = interactionRepository.findByCustomerId(id);
        logger.info("Retrieved {} interactions for customer with id {}", interactions.size(), id);
        return interactions;
    }

    @Override
    public Interaction findInteractionById(Long id, Long interactionId){
        Interaction interaction = interactionRepository.findByCustomerIdAndId(id, interactionId);
        if (interaction != null) {
            logger.info("Retrieved interaction with id {} for customer with id {}", interactionId, id);
        } else {
            logger.info("No interaction with id {} found for customer with id {}", interactionId, id);
        }
        return interaction;

    }

}
