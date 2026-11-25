package station.config.modelConfig;

import station.enumerator.ModuleEnum;
import station.enumerator.RessourceEnum;

public record ModuleConsoConfig(
    ModuleEnum module,
    Integer nbAstroAffectes,
    RessourceEnum ressource,
    Double valeur
) {}
