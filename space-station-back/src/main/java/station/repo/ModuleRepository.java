package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import station.model.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer>{
    
}