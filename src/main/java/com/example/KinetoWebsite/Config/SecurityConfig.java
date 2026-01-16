package com.example.KinetoWebsite.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 1. Resurse statice și pagini publice - ACCES LIBER
                        .requestMatchers(
                                "/", "/error", "/favicon.ico", "/login", "/public",
                                "/css/**", "/js/**", "/images/**",
                                "/gdpr.html/**", "/anpc.html/**", "/termeni.html", "/cookies.html/**",
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                "/api/public/**" // Serviciile (preturi, detalii) sunt publice
                        ).permitAll()

                        // 2. CONFIGURARE SPECIALĂ PENTRU PROGRAMĂRI (/api/appointments)
                        // Oricine poate CREA o programare (POST)
                        .requestMatchers(HttpMethod.POST, "/api/appointments").permitAll()
                        // Doar ADMINUL poate VEDEA, MODIFICA sau ȘTERGE programările
                        .requestMatchers("/api/appointments/**").hasRole("ADMIN")

                        // 3. Tot ce ține de admin necesită rolul ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Orice altceva cere autentificare
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // 4. CONFIGURARE CSRF
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // AICI ESTE MODIFICAREA PRINCIPALĂ: Adăugăm "/api/admin/**" la excepții
                        .ignoringRequestMatchers(
                                "/api/appointments/**",
                                "/api/public/**",
                                "/api/admin/**"
                        )
                )
                .httpBasic(httpBasic -> {});

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username(adminUsername)
                .password(adminPassword)
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}