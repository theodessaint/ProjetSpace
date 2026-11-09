package station.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import station.model.Module;

public interface IDAOModule extends JpaRepository<Module, Integer>{
    
}