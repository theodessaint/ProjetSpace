package station.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import station.model.StationSpatiale;

public interface IDAOStationSpatiale extends JpaRepository<StationSpatiale, Integer>{

}
