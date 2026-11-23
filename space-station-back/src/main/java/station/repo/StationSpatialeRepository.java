package station.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import station.model.StationSpatiale;

public interface StationSpatialeRepository extends JpaRepository<StationSpatiale, Integer>{

    List<StationSpatiale> findByJoueurId(Integer joueurId);

}
