package station.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import station.model.Joueur;

public interface IDAOJoueur extends JpaRepository<Joueur, Integer> {

    public Optional<Joueur> findByLogin(String login);

}
