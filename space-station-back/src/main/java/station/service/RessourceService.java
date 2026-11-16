package station.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.model.Ressource;
import station.repo.RessourceRepository;

@Service
public class RessourceService {
    private final RessourceRepository repository;

    public RessourceService(RessourceRepository repository) {
        this.repository = repository;
    }

    public List<Ressource> findAll() {
        return this.repository.findAll();
    }

    public Ressource findById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ressource introuvable"));
    }

    public Ressource save(Ressource ressource) {
        ressource.setId(null);
        return this.repository.save(ressource);
    }

    public Ressource save(Integer id, Ressource ressource) {
        this.findById(id);
        ressource.setId(id);
        return this.repository.save(ressource);
    }

    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }
}