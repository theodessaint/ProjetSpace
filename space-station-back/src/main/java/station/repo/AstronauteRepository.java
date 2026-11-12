package station.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import station.enumerator.RoleEnum;
import station.model.Astronaute;

public interface AstronauteRepository extends JpaRepository<Astronaute, Integer> {

    List<Astronaute> findByRole(RoleEnum role);

}
