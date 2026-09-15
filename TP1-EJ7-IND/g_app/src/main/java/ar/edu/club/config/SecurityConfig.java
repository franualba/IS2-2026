package ar.edu.club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Seguridad de borde: recursos estáticos y login público; operaciones de gestión requieren
 * rol ADMIN. En producción UserDetails debe migrarse a una entidad/tabla de usuarios.
 */
@Configuration
public class SecurityConfig {
    @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.requestMatchers("/css/**", "/login").permitAll().anyRequest().hasRole("ADMIN"))
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/familias", true).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout")).build();
    }
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService users(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(User.withUsername("admin").password(encoder.encode("admin")) .roles("ADMIN").build());
    }
}
