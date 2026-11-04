package station.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import station.model.Compte;

public interface IDAOCompte extends JpaRepository<Compte, Integer>{
    
}
