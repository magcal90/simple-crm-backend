package sg.edu.ntu.simple_crm.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import sg.edu.ntu.simple_crm.model.AppUser;
import sg.edu.ntu.simple_crm.repository.AppUserRepository;

@Component
public class UserDataLoader implements ApplicationRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final String bootstrapEmail;
    private final String bootstrapPassword;

    public UserDataLoader(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap-user.email:}") String bootstrapEmail,
            @Value("${app.bootstrap-user.password:}") String bootstrapPassword) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapEmail = bootstrapEmail;
        this.bootstrapPassword = bootstrapPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (bootstrapEmail.isBlank() || bootstrapPassword.isBlank()
                || appUserRepository.findByEmail(bootstrapEmail).isPresent()) {
            return;
        }

        appUserRepository.save(AppUser.builder()
                .email(bootstrapEmail)
                .passwordHash(passwordEncoder.encode(bootstrapPassword))
                .roles(Set.of("USER"))
                .enabled(true)
                .build());
    }
}
