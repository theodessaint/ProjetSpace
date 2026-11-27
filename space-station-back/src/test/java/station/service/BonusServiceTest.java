package station.service;

import static org.junit.jupiter.api.Assertions.assertSame;
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

import station.config.PartieConfig;
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
    @DisplayName ("deleteById supprime le bonus dans le repository")
    void deleteById(){
        service.deleteById(5);

        verify(repository).deleteById(5);
    }


}
