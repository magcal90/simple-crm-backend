package sg.edu.ntu.simple_crm.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "customer")
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotBlank(message = "First name is mandatory")
  @Column(name = "first_name")
  private String firstName;

  @NotBlank(message = "Last name is mandatory")
  @Column(name = "last_name")
  private String lastName;

  @Email(message = "Email should be valid")
  @NotBlank(message = "Email is mandatory")
  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "company")
  private String company;

  @Builder.Default
  @Column(name = "status", nullable = false)
  private String status = "active";

  @Builder.Default
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "customer_tags", joinColumns = @JoinColumn(name = "customer_id"))
  @Column(name = "tag")
  private List<String> tags = new ArrayList<>();

  @Column(name = "notes", length = 4000)
  private String notes;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDate createdAt;

  @PrePersist
  void initializeDefaults() {
    if (status == null || status.isBlank()) {
      status = "active";
    }
    if (tags == null) {
      tags = new ArrayList<>();
    }
    if (createdAt == null) {
      createdAt = LocalDate.now();
    }
  }

}
