package sg.edu.ntu.simple_crm.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.edu.ntu.simple_crm.model.InteractionType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractionRequest {

    @NotNull(message = "Remarks are mandatory")
    @Size(min = 10, max = 1025, message = "Remarks must be between 10 and 1025 characters")
    private String remarks;

    @FutureOrPresent(message = "Interaction date must be today or in the future")
    private LocalDate interactionDate;

    @NotNull(message = "Interaction type is mandatory")
    private InteractionType interactionType;
}
