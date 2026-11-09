package station.dao;

import station.model.Astronaute;
import station.model.Role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IDAOAstronaute extends JpaRepository<Astronaute, Integer> {

    List<Astronaute> findByRole(Role role);

}
