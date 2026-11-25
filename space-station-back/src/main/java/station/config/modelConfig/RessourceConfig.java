package station.config.modelConfig;

import station.enumerator.RessourceEnum;

public record RessourceConfig(
    RessourceEnum type,
    Double start,
    Double danger,
    Double critique
) {}
