package station.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import station.security.service.JwtService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    // Constructeur de SecurityConfig avec création de JwtService
    public SecurityConfig(JwtService jwtService) {}
    
    @Bean
    JwtEncoder jwtEncoder(JwtService jwtService) {
        return jwtService.jwtEncoder();
    }
    
    @Bean
    JwtDecoder jwtDecoder(JwtService jwtService, @Value("{${spring.application.name}}") String issuer) {
        return jwtService.jwtDecoder(issuer);
    }
    
    // Création des filtres avec les différentes configurations
    @Bean
    SecurityFilterChain filterChain(
        HttpSecurity http, 
        JwtAuthenticationConverter jwtAuthenticationConverter,
        CorsConfigurationSource corsConfigurationSource
    ) throws Exception {
        // Désactivation des sessions HTTP - ici c'est le token JWT qui va servir d'authentification pour chacune des pages, hormis les pages accessibles à tout le monde
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        // Désactivation de la protection CSRF uniquement pour certaines URLs
        // Cross-Site Request Forgery - attaque qui pousse un utilisateur authentifié (par cookie de session, par exemple) d'effectuer une action à son insu
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/auth", "/joueur/inscription"));
        
        // Configuration CORS (personnalisée plus bas)
        http.cors(cors -> cors.configurationSource(corsConfigurationSource));
        
        // Réactivation de l'authentification HTTP Basic
        http.httpBasic(Customizer.withDefaults());
        
        // Activation du filtre de validation du jeton JWT (vérifie les autorisations)
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
            .jwtAuthenticationConverter(jwtAuthenticationConverter))
        );
        
        // Règles des autorisations
        http.authorizeHttpRequests(authorization -> {
            authorization.requestMatchers("/auth", "/joueur/inscription").permitAll();
            authorization.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN");
            authorization.requestMatchers("/admin").hasRole("ADMIN");
            authorization.anyRequest().authenticated();
        });
        
        return http.build();
    }
    
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Extraction des autorisations contenu dans le token, provenant elles-mêmes de l'utilisateur lors de sa connexion où son rôle est attribué (voir JpaUserDetailsService)
        JwtGrantedAuthoritiesConverter granted = new JwtGrantedAuthoritiesConverter();
        
        // Création d'un convertisseur principal
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        
        // Suppression du préfixe, par défautpréfixe = "SCOPE_"
        granted.setAuthorityPrefix("");
        
        // Association au convertisseur principal
        converter.setJwtGrantedAuthoritiesConverter(granted);
        
        return converter;
    }
    
    // Configuration Cross-Origin Resource Sharing - permet à un serveur d'indiquer aux navigateurs quelles applications web peuvent accéder à ses ressources
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        
        corsSource.registerCorsConfiguration("/**", corsConfiguration);
        
        return corsSource;
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
