package sg.edu.ntu.simple_crm.config;

import java.util.List;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sg.edu.ntu.simple_crm.security.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final JwtAuthFilter jwtAuthFilter;
        private final List<String> allowedOrigins;

        public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                        @Value("${app.cors.allowed-origins}") String allowedOrigins) {
                this.jwtAuthFilter = jwtAuthFilter;
                this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                                .map(String::trim)
                                .filter(origin -> !origin.isEmpty())
                                .toList();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                // REST APIs using stateless JWT auth do not need CSRF protection.
                                // CSRF is designed for browser-based session flows; since we never issue
                                // a session cookie, there is nothing for a cross-site request to hijack.
                                .cors(Customizer.withDefaults())
                                .csrf(csrf -> csrf.disable())

                                // Stateless: Spring Security will not create or use sessions.
                                // Every request must carry a valid JWT token.
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // /auth/login is public; everything else needs a valid token
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/auth/login").permitAll()
                                                .anyRequest().authenticated())

                                // Register JWT filter to run before username/password authentication
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(allowedOrigins);
                configuration.setAllowedMethods(
                                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(
                                List.of("Content-Type", "Authorization"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService(
                        PasswordEncoder passwordEncoder,
                        @Value("${app.demo-users.user-password:p@55w0rd123}") String userPassword,
                        @Value("${app.demo-users.admin-password:p@55w0rd123}") String adminPassword,
                        @Value("${app.demo-users.manager-password:p@55w0rd123}") String managerPassword) {
                return new InMemoryUserDetailsManager(
                                User.withUsername("user@example.com")
                                                .password(passwordEncoder.encode(userPassword))
                                                .roles("USER")
                                                .build(),
                                User.withUsername("admin@example.com")
                                                .password(passwordEncoder.encode(adminPassword))
                                                .roles("ADMIN")
                                                .build(),
                                User.withUsername("manager@example.com")
                                                .password(passwordEncoder.encode(managerPassword))
                                                .roles("MANAGER")
                                                .build());
        }

}
