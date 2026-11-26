package station.config;

import java.util.List;
import java.util.Map;

import station.config.modelConfig.BonusConfig;
import station.config.modelConfig.ModuleConfig;
import station.config.modelConfig.ModuleConsoConfig;
import station.config.modelConfig.ModuleProdConfig;
import station.config.modelConfig.RessourceConfig;
import station.enumerator.RoleEnum;

public record PartieConfig(
    int cycleFin,
    int nbAstronautesDepart,
    int freqCyclesArriveeAstro,
    int freqCyclesDeblocageModule,
    int coutEnergieDeblocageModule,
    Map<RoleEnum, Integer> nbAstronautesMaxParRole,
    List<BonusConfig> bonus,
    List<ModuleConfig> modules,
    List<ModuleConsoConfig> moduleConso,
    List<ModuleProdConfig> moduleProd,
    List<RessourceConfig> ressources
) {
    public int getNbAstronautesMax(RoleEnum role) {
        return nbAstronautesMaxParRole.getOrDefault(
            role, 0
        );
    }
    
    public int getNbAstronautesDepart() {
        return nbAstronautesDepart;
    }
    
    public int getFreqCyclesArriveeAstro() {
        return freqCyclesArriveeAstro;
    }
    
    public int getFreqCyclesDeblocageModule() {
        return freqCyclesDeblocageModule();
    }

    public int getCoutEnergieDeblocageModule() {
        return coutEnergieDeblocageModule();
    }
}