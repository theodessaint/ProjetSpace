package station.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.model.Module;
import station.repo.ModuleRepository;

@Service
public class ModuleService {
    
    private static final int cyclesAvantDeblocageModule = 5;
    private final ModuleRepository repository;

    public ModuleService(ModuleRepository repository) {
        this.repository = repository;
    }

    public List<Module> findAll() {
        return this.repository.findAll();
    }

    public Module findById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module introuvable"));
    }

    public Module save(Module module) {
        module.setId(null);
        return this.repository.save(module);
    }

    public Module save(Integer id, Module module) {
        this.findById(id);
        module.setId(id);
        return this.repository.save(module);
    }

    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }
}