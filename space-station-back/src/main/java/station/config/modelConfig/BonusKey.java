package station.config.modelConfig;

import station.enumerator.ModuleEnum;
import station.enumerator.RessourceEnum;
import station.enumerator.RoleEnum;

public record BonusKey(
    RoleEnum role,
    ModuleEnum module,
    RessourceEnum ressource
) {}
