package edu.byui.apj.storefront.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security: form login, in-memory users with roles, and USER-only profile routes.
 */
@Configuration
public class SecurityConfig {

    /**
     * BCrypt hashes passwords at startup for the in-memory users.
     * Same pattern as Article 13-2 in the course materials.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Two demo principals:
     * - "shopper" has ROLE_USER only (can use the profile area).
     * - "manager" has ROLE_USER and ROLE_ADMIN (still allowed on USER-only routes).
     * Spring stores roles as authorities like ROLE_USER, ROLE_ADMIN.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails shopper = User.builder()
                .username("shopper")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails manager = User.builder()
                .username("manager")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(shopper, manager);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Course/demo only: without this, HTML forms need CSRF tokens.
                // Production apps should keep CSRF enabled and supply tokens properly.
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Only these require an authenticated user with ROLE_USER
                        .requestMatchers("/profile.html", "/api/me/**").hasRole("USER")
                        // Everything else (home, products, cart, checkout, login page, static assets, etc.)
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        // GET shows your static login page (provided by instructor)
                        .loginPage("/login.html")
                        // POST is handled by Spring Security (username + password fields)
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                        .permitAll()
                );

        return http.build();
    }
}
