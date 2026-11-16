package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import station.model.Ressource;

public interface RessourceRepository extends JpaRepository<Ressource, Integer> {
}