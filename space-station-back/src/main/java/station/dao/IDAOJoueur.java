package station.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import station.model.Joueur;

public interface IDAOJoueur extends JpaRepository<Joueur, Integer> {

}
