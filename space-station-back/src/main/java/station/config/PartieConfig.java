package station.config;

import java.util.List;

public record PartieConfig(
    List<BonusConfig> bonus,
    List<ModuleConfig> modules,
    List<ModuleConsoConfig> moduleConso,
    List<ModuleProdConfig> moduleProd,
    List<RessourceConfig> ressources
) {}