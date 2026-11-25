package station.api.request;

import jakarta.validation.constraints.NotNull;

public class AffecterAstronauteRequest {

    @NotNull 
    private Integer moduleId;

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}