package station.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import station.api.request.CreateOrUpdateCompteRequest;
import station.exception.CompteNotFoundException;
import station.model.Compte;
import station.repo.CompteRepository;

@ExtendWith(MockitoExtension.class)
public class CompteServiceTest {
    
    @Mock
    private CompteRepository repository;   // faux repository fabrique par l'extension

    @InjectMocks
    private CompteService service;         // Mockito appelle le constructeur avec le mock ci-dessus

    // petit utilitaire : evite de reecrire les deux setters dans chaque test
    private CreateOrUpdateCompteRequest requete(String login, String password) {
        CreateOrUpdateCompteRequest requete = new CreateOrUpdateCompteRequest();
        requete.setLogin(login);
        requete.setPassword(password);
        return requete;
    }

    // ------ findAll / findById / findByUsername -------

    @Test 
    @DisplayName ("Find all")
    void findAll(){
        List<Compte> comptes = List.of(new Compte(1, "login", "password"));
        when(repository.findAll()).thenReturn(comptes);

        assertSame(comptes, service.findAll());
    }

    @Test 
    @DisplayName ("Find by id")
    void findById(){
        Compte compte = new Compte(1, "login", "password");
        when(repository.findById(1)).thenReturn(Optional.of(compte));
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertSame(compte, service.findById(1));
        assertThrows(CompteNotFoundException.class, () -> service.findById(99));
    }

    @Test 
    @DisplayName ("Find by username")
    void findByUsername(){
        Compte compte = new Compte(1, "login", "password");
        when(repository.findByLogin("login")).thenReturn(Optional.of(compte));
        when(repository.findByLogin("test")).thenReturn(Optional.empty());

        assertSame(compte, service.findByUsername("login"));
        assertThrows(CompteNotFoundException.class, () -> service.findByUsername("test"));
    }

    @Test 
    @DisplayName ("Save")
    void save(){
        when(repository.save(any(Compte.class))).thenAnswer(appel -> appel.getArgument(0));

        Compte compte = service.save(requete("login", "password"));

        assertEquals("login", compte.getLogin());
        assertEquals("password", compte.getPassword());

        verify(repository).save(any(Compte.class));
    }

    @Test 
    @DisplayName ("Modifier")
    void modifier(){
        Compte compte = new Compte(1, "login", "password");

        when(repository.findById(1)).thenReturn((Optional.of(compte)));
        when(repository.save(any(Compte.class))).thenAnswer(appel -> appel.getArgument(0));

        Compte compte2 = service.save(1, requete("login2", "password2"));

        assertSame(compte, compte2);

        assertEquals("login2", compte2.getLogin());
        assertEquals("password2", compte2.getPassword());
    }

    @Test 
    @DisplayName ("Delete")
    void deleteById(){
        service.deleteById(1);

        verify(repository).deleteById(1);
    }
}
