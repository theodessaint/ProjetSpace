package station.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.model.Bonus;
import station.repo.BonusRepository;

@Service
public class BonusService {
    private final BonusRepository repository;

    public BonusService(BonusRepository repository) {
        this.repository = repository;
    }

    public List<Bonus> findAll() {
        return this.repository.findAll();
    }

    public Bonus findById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bonus introuvable"));
    }

    public Bonus save(Bonus bonus) {
        bonus.setId(null);
        return this.repository.save(bonus);
    }

    public Bonus save(Integer id, Bonus bonus) {
        this.findById(id);
        bonus.setId(id);
        return this.repository.save(bonus);
    }

    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }
}