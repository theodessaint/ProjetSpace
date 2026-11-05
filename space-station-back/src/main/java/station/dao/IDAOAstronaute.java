package station.dao;

import station.model.Astronaute;
import station.model.Genre;
import station.model.Role;

import java.util.List;
import java.util.Optional;

public interface IDAOAstronaute {

    List<Astronaute> findByRole(Role role);
    
}
