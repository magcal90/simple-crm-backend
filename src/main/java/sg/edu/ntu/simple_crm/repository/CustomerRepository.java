package sg.edu.ntu.simple_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.ntu.simple_crm.model.Customer;
import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, Long>{

    // find all customers by firstName
    List<Customer> findByFirstName(String firstName);

    boolean existsByEmail(String email);
}
