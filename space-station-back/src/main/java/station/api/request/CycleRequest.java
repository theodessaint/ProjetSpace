package station.api.request;

import java.util.Map;

import station.enumerator.RessourceEnum;

public record CycleRequest(
    Map<RessourceEnum, Double> stats
) {}
