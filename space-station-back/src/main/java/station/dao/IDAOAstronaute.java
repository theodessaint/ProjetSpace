package station.dao;

import station.enumerator.RoleEnum;
import station.model.Astronaute;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import station.enumerator.RoleEnum;
import station.model.Astronaute;

public interface IDAOAstronaute extends JpaRepository<Astronaute, Integer> {

    List<Astronaute> findByRole(RoleEnum role);

}
