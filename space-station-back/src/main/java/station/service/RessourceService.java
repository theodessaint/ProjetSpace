package station.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import station.enumerator.RessourceEnum;
import station.model.Ressource;
import station.model.StationSpatiale;

@Service
public class RessourceService {
    private static final Logger log = LoggerFactory.getLogger(RessourceService.class);
    private final StationSpatialeService stationService;
    
    public RessourceService(StationSpatialeService stationService) {
        this.stationService = stationService;
    }
    
    public Map<Ressource, Double> getRessources(Integer stationId) {
        log.debug("Liste des types de ressources ...");
        
        return this.stationService.findById(stationId).getStats();
    }
    
  
    @Transactional
    public double getQuantite(Integer stationId, RessourceEnum type) {
        StationSpatiale station = stationService.findById(stationId);
        log.debug("Récupération de la quantité pour la ressource {} pour la station {} ...", type, stationId);

        return station.getQuantite(type);
    }

    @Transactional
    public void setQuantite(
            Integer stationId,
            RessourceEnum type,
            double variation
    ) {
        StationSpatiale station = stationService.findById(stationId);
        log.debug("Modification de la quantité pour la ressource {} pour la station {} ...", type, stationId);

        station.setQuantite(type, variation);
    }
}