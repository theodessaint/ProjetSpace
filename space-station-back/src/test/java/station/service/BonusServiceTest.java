package station.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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

import station.api.request.CreateOrUpdateBonusRequest;
import station.config.PartieConfig;
import station.enumerator.RessourceEnum;
import station.enumerator.RoleEnum;
import station.exception.BonusNotFoundException;
import station.model.Bonus;
import station.repo.BonusRepository;

@ExtendWith(MockitoExtension.class)
public class BonusServiceTest {

    @Mock
    private BonusRepository repository;

    @Mock
    private PartieConfig config;

    @InjectMocks
    private BonusService service;
    
    // ------ findAll / finById / deleteById -------
    @Test 
    @DisplayName("findAll retourne les bonus du repository")
    void findAll(){
        List<Bonus> bonus = List.of(new Bonus(), new Bonus());
        when(repository.findAll()).thenReturn(bonus);

        assertSame(bonus, service.findAll());   
    }
    @Test 
    @DisplayName ("findById retourne le bonus trouvé")
    void findById(){
        Bonus bonus = new Bonus();
        when(repository.findById(1)).thenReturn(Optional.of(bonus));

        assertSame(bonus, service.findById(1));
    }
    @Test
    @DisplayName ("findById lance BonusNotFoundException si le bonus n'existe pas")
    void findByIdBonusNotFoundException(){
        when(repository.findById(88)).thenReturn(Optional.empty());

        assertThrows(BonusNotFoundException.class, () -> service.findById(88));
    }
    @Test 
    @DisplayName ("deleteById supprime le bonus dans le repository")
    void deleteById(){
        service.deleteById(5);

        verify(repository).deleteById(5);
    }
    // ------ save(request) / save(id, request) -------
    @Test
    @DisplayName("save(request) crée un bonus avec les valeurs de la requête")
    void saveCreation() {
        CreateOrUpdateBonusRequest request = mock(CreateOrUpdateBonusRequest.class);
        when(request.getRole()).thenReturn(RoleEnum.Agriculteur);
        when(request.getRessource()).thenReturn(RessourceEnum.NOURRITURE);
        when(request.getMultiplicateur()).thenReturn(1.5);
        when(repository.save(any(Bonus.class))).thenAnswer(i -> i.getArgument(0));

        Bonus resultat = service.save(request);

        assertEquals(RoleEnum.Agriculteur, resultat.getRole());
        assertEquals(RessourceEnum.NOURRITURE, resultat.getRessource());
        assertEquals(1.5, resultat.getMultiplicateur(), 0.001);
    }

}
