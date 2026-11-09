package station.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import station.model.Module;

public class IDAOModule extends JpaRepository<Module, Integer>{
    
}