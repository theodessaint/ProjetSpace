package station.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import station.model.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer>{
    
    List<Module> findByStationId(Integer stationId);

}