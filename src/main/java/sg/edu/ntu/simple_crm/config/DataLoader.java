package sg.edu.ntu.simple_crm.config;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import sg.edu.ntu.simple_crm.model.Customer;
import sg.edu.ntu.simple_crm.repository.CustomerRepository;

@Component
public class DataLoader implements ApplicationRunner {

    private final CustomerRepository customerRepository;

    public DataLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        sampleCustomers().stream()
                .filter(customer -> !customerRepository.existsByEmail(customer.getEmail()))
                .forEach(customerRepository::save);
    }

    private List<Customer> sampleCustomers() {
        return List.of(
                customer("Tony", "Stark", "tony.stark@example.com", "Stark Industries"),
                customer("Bruce", "Banner", "bruce.banner@example.com", "Avengers"),
                customer("Peter", "Parker", "peter.parker@example.com", "Daily Bugle"),
                customer("Stephen", "Strange", "stephen.strange@example.com", "Sanctum Sanctorum"));
    }

    private Customer customer(String firstName, String lastName, String email, String company) {
        return Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .company(company)
                .status("active")
                .build();
    }
}
