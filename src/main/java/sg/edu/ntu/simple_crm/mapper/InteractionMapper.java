package sg.edu.ntu.simple_crm.mapper;

import org.springframework.stereotype.Component;

import sg.edu.ntu.simple_crm.dto.InteractionRequest;
import sg.edu.ntu.simple_crm.dto.InteractionResponse;
import sg.edu.ntu.simple_crm.model.Interaction;

@Component
public class InteractionMapper {

    public Interaction toEntity(InteractionRequest request) {
        if (request == null) {
            return null;
        }

        Interaction interaction = new Interaction();
        interaction.setRemarks(request.getRemarks());
        interaction.setInteractionDate(request.getInteractionDate());
        interaction.setInteractionType(request.getInteractionType());
        return interaction;
    }

    public InteractionResponse toResponse(Interaction interaction) {
        if (interaction == null) {
            return null;
        }

        return InteractionResponse.builder()
                .id(interaction.getId())
                .remarks(interaction.getRemarks())
                .interactionDate(interaction.getInteractionDate())
                .interactionType(interaction.getInteractionType())
                .customerId(interaction.getCustomer() == null ? null : interaction.getCustomer().getId())
                .build();
    }
}
