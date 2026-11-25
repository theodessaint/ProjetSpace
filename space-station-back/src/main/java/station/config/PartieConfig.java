package station.config;

import java.util.List;

import station.config.modelConfig.BonusConfig;
import station.config.modelConfig.ModuleConfig;
import station.config.modelConfig.ModuleConsoConfig;
import station.config.modelConfig.ModuleProdConfig;
import station.config.modelConfig.RessourceConfig;

public record PartieConfig(
    List<BonusConfig> bonus,
    List<ModuleConfig> modules,
    List<ModuleConsoConfig> moduleConso,
    List<ModuleProdConfig> moduleProd,
    List<RessourceConfig> ressources
) {}