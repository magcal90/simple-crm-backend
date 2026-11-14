package sg.edu.ntu.simple_crm.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import sg.edu.ntu.simple_crm.model.AppUser;
import sg.edu.ntu.simple_crm.repository.AppUserRepository;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseAuthenticationTest {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
    }

    @Test
    void authenticatesUsingBcryptPasswordStoredInDatabase() {
        String rawPassword = "database-only-password";
        AppUser user = appUserRepository.save(AppUser.builder()
                .email("db-user@example.com")
                .passwordHash(passwordEncoder.encode(rawPassword))
                .roles(Set.of("USER"))
                .enabled(true)
                .build());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), rawPassword));

        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .contains("ROLE_USER");
        assertThat(appUserRepository.findByEmail(user.getEmail()).orElseThrow().getPasswordHash())
                .startsWith("$2")
                .isNotEqualTo(rawPassword);
        assertThat(userDetailsService).isInstanceOf(DatabaseUserDetailsService.class);
    }
}
