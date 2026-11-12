package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import station.model.Compte;

public interface CompteRepository extends JpaRepository<Compte, Integer>{
   
    public Optional<Compte> findByUsername(String username);

}
