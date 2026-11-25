package station.config.modelConfig;

import station.enumerator.ModuleEnum;

public record ModuleConfig(
    ModuleEnum module, 
    Integer nbModuleMax, 
    Integer nbAstroModuleMax
) {}