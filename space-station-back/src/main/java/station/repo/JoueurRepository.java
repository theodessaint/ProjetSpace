package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import station.model.Joueur;

public interface JoueurRepository extends JpaRepository<Joueur, Integer> {

    public Optional<Joueur> findByLogin(String login);

}
