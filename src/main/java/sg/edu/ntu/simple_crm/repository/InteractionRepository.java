package sg.edu.ntu.simple_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import sg.edu.ntu.simple_crm.model.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction , Long> {
    List<Interaction> findByCustomerId(Long id);
    Interaction findByCustomerIdAndId(Long customerId, Long interactionId);
    
}
