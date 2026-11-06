package sg.edu.ntu.simple_crm.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.edu.ntu.simple_crm.model.InteractionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionResponse {

    private Long id;
    private String remarks;
    private LocalDate interactionDate;
    private InteractionType interactionType;
    private Long customerId;
}
