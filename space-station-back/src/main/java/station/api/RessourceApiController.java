package station.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import station.enumerator.RessourceEnum;
import station.service.RessourceService;

@RestController
@RequestMapping("/station/{stationId}/ressources")
public class RessourceApiController {
    private final RessourceService service;

    public RessourceApiController(RessourceService service) {
        this.service = service;
    }

    @GetMapping("/{type}")
    public double getQuantite(
            @PathVariable Integer stationId,
            @PathVariable RessourceEnum type
    ) {
        return this.service.getQuantite(stationId, type);
    }
}