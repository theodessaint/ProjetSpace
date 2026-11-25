package station.config.modelConfig;

import station.enumerator.ModuleEnum;
import station.enumerator.RessourceEnum;
import station.enumerator.RoleEnum;

public record BonusConfig(
    RoleEnum role,
    ModuleEnum module,
    RessourceEnum ressource,
    Double multiplicateur
) {
    public BonusKey key() {
        return new BonusKey(role, module, ressource);
    }
}