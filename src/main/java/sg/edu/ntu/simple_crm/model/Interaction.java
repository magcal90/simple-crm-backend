package sg.edu.ntu.simple_crm.model;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter 
@Setter 
@AllArgsConstructor 
@EqualsAndHashCode 
@NoArgsConstructor
@Entity 
@Table(name = "interaction")
public class Interaction {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Length(min = 10, max = 1025)
    @NotNull
    @Column(name = "remarks")
    private String remarks;

    @FutureOrPresent 
    @Column (name = "interaction_date")
    private LocalDate interactionDate;

    @NotNull 
    @Column(name = "interaction_type")
    private InteractionType interactionType;

    @ManyToOne(optional =false)
    @JoinColumn (name ="customer_id" , referencedColumnName = "id")
    private Customer customer;
    
}
