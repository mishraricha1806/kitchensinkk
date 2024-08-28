package org.example.kitchensinkk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/members/login", "/static/**", "/error").permitAll() // Allow access to login, static resources, and error pages
                .requestMatchers("/members/api/**").hasAnyRole("READ_ONLY", "READ_WRITE") // Secure API endpoints
                .requestMatchers("/members/**").authenticated() // Allow any authenticated user to access /members
                .anyRequest().authenticated() // All other requests require authentication
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/members/login") // Custom login page
                .defaultSuccessUrl("/members", true) // Redirect to /members upon successful login
                .failureUrl("/members/login?error=true") // Redirect to /login on failure with an error message
                .permitAll() // Allow everyone to see the login page
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Custom logout URL
                .logoutSuccessUrl("/members/login?logout") // Redirect to login page with logout parameter on successful logout
                .permitAll()
            )
            .csrf().disable(); // Disable CSRF for simplicity; consider enabling in production with proper CSRF token handling

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Define users with roles for in-memory authentication
        UserDetails user1 = User.withUsername("user1")
                .password(passwordEncoder().encode("password"))
                .roles("READ_ONLY")
                .build();

        UserDetails user2 = User.withUsername("user2")
                .password(passwordEncoder().encode("password"))
                .roles("READ_WRITE")
                .build();
System.out.println("user 2 is in making"+user2);
        return new InMemoryUserDetailsManager(user1, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    
}
