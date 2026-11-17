package station.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import station.repo.CompteRepository;
import station.model.Admin;

@Service 
public class JpaUserDetailsService implements UserDetailsService {
    private final CompteRepository repository;

    public JpaUserDetailsService(CompteRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repository
            // Recherche de l'utilisateur par son username
            .findByLogin(username)

            // Transformation de Utilisateur en UserDetails
            .map(personne -> User
                .withUsername(username)
                .password(personne.getPassword())
                .roles(personne instanceof Admin? "ADMIN" : "JOUEUR")
                .build()
            )

            // Si l'utilisateur n'existe pas, on jète l'exception UsernameNotFoundException
            .orElseThrow(() -> new UsernameNotFoundException("Username not found"))
        ;
    } 
}
