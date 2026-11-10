package station.dao;

import station.enumerator.RoleEnum;
import station.model.Astronaute;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IDAOAstronaute extends JpaRepository<Astronaute, Integer> {

    List<Astronaute> findByRole(RoleEnum role);

}
