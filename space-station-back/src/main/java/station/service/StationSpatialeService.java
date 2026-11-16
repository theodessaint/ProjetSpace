package station.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import station.model.Module;
import station.model.Position;
import station.enumerator.ModuleEnum;
import station.exception.PositionNotFoundException;
import station.model.Emplacement;
import station.model.Grille;
import station.model.StationSpatiale;
import station.repo.ModuleRepository;
import station.repo.StationSpatialeRepository;

@Service
public class StationSpatialeService {
    private final StationSpatialeRepository repository;
    private final ModuleRepository moduleRepository;

    public StationSpatialeService(StationSpatialeRepository repository, ModuleRepository moduleRepository) {
        this.repository = repository;
        this.moduleRepository = moduleRepository;
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
        StationSpatiale stationCree = this.repository.save(station);
        this.creerModules(stationCree);
        return stationCree;
    }

    public StationSpatiale save(Integer id, StationSpatiale station) {
        this.findById(id);
        station.setId(id);
        return this.repository.save(station);
    }

    public void deleteById(Integer id) {
        this.repository.deleteById(id);
    }

    public void creerModules(StationSpatiale station) {
        List<Module> modules = new ArrayList<>();

        for(Emplacement emplacement : Grille.PLAN) {
            Position position = emplacement.getPosition();

            if(!Grille.contient(position)){
                throw new PositionNotFoundException();
            }

            Module module = new Module();

            module.setNom(emplacement.getType().name());

            module.setPosition(new Position(position.getX(), position.getX()));
            module.setDebloque(emplacement.getType() == ModuleEnum.CentreDeCommandement || emplacement.getType() == ModuleEnum.Dortoir);

            station.ajouterModule(module);

            modules.add(module);
        }

        this.moduleRepository.saveAll(modules);
    }
}