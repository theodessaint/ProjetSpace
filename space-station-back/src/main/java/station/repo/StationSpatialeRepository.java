package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import station.model.StationSpatiale;

public interface StationSpatialeRepository extends JpaRepository<StationSpatiale, Integer>{

}
