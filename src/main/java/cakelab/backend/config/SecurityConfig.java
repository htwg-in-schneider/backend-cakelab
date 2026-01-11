package cakelab.backend.config;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Benutzt das unten definierte Bean 'corsConfigurationSource'
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. CSRF deaktivieren (notwendig für PATCH/POST mit JWT)
                .csrf(csrf -> csrf.disable()) 
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/profile").authenticated()
                        .requestMatchers("/api/orders", "/api/orders/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/cake", "/api/cake/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/cake/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/cake/*").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/cake", "/api/cake/*").permitAll()
                        .requestMatchers("/api/**").permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults()))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        configuration.setAllowCredentials(true);       
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}