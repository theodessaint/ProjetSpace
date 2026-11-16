package station.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.model.StationSpatiale;
import station.repo.StationSpatialeRepository;

@Service
public class StationSpatialeService {
    private final StationSpatialeRepository repository;

    public StationSpatialeService(StationSpatialeRepository repository) {
        this.repository = repository;
    }

    public List<StationSpatiale> findAll() {
        return this.repository.findAll();
    }

    public StationSpatiale findById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Station introuvable"));
    }

    public StationSpatiale save(StationSpatiale station) {
        station.setId(null);
        return this.repository.save(station);
    }

    public StationSpatiale save(Integer id, StationSpatiale station) {
        this.findById(id);
        station.setId(id);
        return this.repository.save(station);
    }

    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }
}