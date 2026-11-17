package station.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import station.api.request.CreateOrUpdateAstronauteRequest;
import station.enumerator.RoleEnum;
import station.exception.AstronauteNotFoundException;
import station.exception.StationSpatialeNotFoundException;
import station.model.Astronaute;
import station.model.StationSpatiale;
import station.repo.AstronauteRepository;
import station.repo.StationSpatialeRepository;

@Service
public class AstronauteService {
    private static final Logger log = LoggerFactory.getLogger(AstronauteService.class);
    private final AstronauteRepository repository;
    private final StationSpatialeRepository stationRepository;
    
    public AstronauteService(AstronauteRepository repository, StationSpatialeRepository stationRepository) {
        this.repository = repository;
        this.stationRepository = stationRepository;
    }
    
    public List<Astronaute> findAll() {
        log.debug("Liste des astronautes ...");
        
        return this.repository.findAll();
    }
    
    public List<Astronaute> findByRole(RoleEnum role) {
        log.debug("Liste des astronautes de rôle {} ...", role);
        
        return this.repository.findByRole(role);
    }
    
    public Astronaute findById(Integer id) {
        log.debug("Recherche de l'astronaute {} ...", id);
        
        return this.repository.findById(id).orElseThrow(
            () -> {
                log.warn("Astronaute introuvable : id={}", id);
                return new AstronauteNotFoundException();
            }
        );
    }
    
    public Astronaute save(CreateOrUpdateAstronauteRequest request) {
        Astronaute astronaute = this.save(new Astronaute(), request);
        log.debug("Astronaute {} ajouté !", astronaute.getId());
        
        return astronaute;
    }
    
    public Astronaute save(Integer id, CreateOrUpdateAstronauteRequest request) {
        Astronaute astronaute = this.findById(id);
        astronaute = this.save(astronaute, request);
        log.debug("Astronaute {} modifié !", id);
        
        return astronaute;
    }
    
    public void deleteById(Integer id) {
        this.repository.deleteById(id);
        log.debug("Astronaute {} supprimé !", id);
    }
    
    private Astronaute save(Astronaute astronaute, CreateOrUpdateAstronauteRequest request) {
        astronaute.setNom(request.getNom());
        astronaute.setGenre(request.getGenre());
        astronaute.setRole(request.getRole());

        StationSpatiale station = stationRepository.findById(request.getStationId())
        .orElseThrow(
            () -> {
                log.warn("Station spatiale introuvable : id={}", request.getStationId());
                return new StationSpatialeNotFoundException();
            }
        );
        astronaute.setStation(station);
        
        return this.repository.save(astronaute);
    }
}