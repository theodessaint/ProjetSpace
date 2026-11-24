package station.config;

import station.enumerator.ModuleEnum;

public record ModuleConfig(
    ModuleEnum module, 
    Integer nbModuleMax, 
    Integer nbAstroModuleMax
) {}