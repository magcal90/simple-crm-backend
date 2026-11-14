package sg.edu.ntu.simple_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import sg.edu.ntu.simple_crm.model.Interaction;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    List<Interaction> findByCustomerId(Long id);

    Interaction findByCustomerIdAndId(Long customerId, Long interactionId);

    @Modifying
    @Query("delete from Interaction i where i.customer.id = :customerId")
    int deleteByCustomerId(@Param("customerId") Long customerId);

}
