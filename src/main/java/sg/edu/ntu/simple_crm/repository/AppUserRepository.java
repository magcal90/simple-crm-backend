package sg.edu.ntu.simple_crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.ntu.simple_crm.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);
}
