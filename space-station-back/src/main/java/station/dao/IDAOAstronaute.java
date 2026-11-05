package station.dao;

import station.model.Astronaute;
import station.model.Genre;
import station.model.Role;

import java.util.List;
import java.util.Optional;

public interface IDAOAstronaute {

    Astronaute save(Astronaute astronaute);

    Optional<Astronaute> findById(Integer id);

    List<Astronaute> findAll();

    List<Astronaute> findByRole(Role role);

    List<Astronaute> findByGenre(Genre genre);

    boolean update(Astronaute astronaute);

    boolean deleteById(Integer id);

    Integer count();
}
