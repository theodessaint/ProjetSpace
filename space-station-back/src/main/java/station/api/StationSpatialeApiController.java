package station.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import station.api.request.CreateOrUpdateStationSpatialeRequest;
import station.api.response.EntityCreatedResponse;
import station.api.response.EntityUpdatedResponse;
import station.api.response.StationSpatialeResponse;
import station.api.response.StationSpatialeResumeResponse;
import station.model.StationSpatiale;
import station.service.ModuleService;
import station.service.StationSpatialeService;

@RestController
@RequestMapping("/station")
public class StationSpatialeApiController {

    private final StationSpatialeService service;
    private final ModuleService moduleService;

    public StationSpatialeApiController(StationSpatialeService service, ModuleService moduleService) {
        this.service = service;
        this.moduleService = moduleService;
    }

    @GetMapping
    public List<StationSpatialeResponse> findAll() {
        return this.service.findAll().stream()
                .map(station -> StationSpatialeResponse.convert(station, this.moduleService))
                .toList();
    }

    @GetMapping("/mes-parties") // Renvoie les parties du joueur connecté sous la forme d'une liste
    public List<StationSpatialeResumeResponse> findMesParties() {

        return this.service.findByLoginJoueur(this.getLoginConnecte()).stream()
                .map(StationSpatialeResumeResponse::convert)
                .toList();

    }

    @GetMapping("/{id}")
    public StationSpatialeResponse findById(@PathVariable Integer id) {

        StationSpatiale station = this.service.findByIdEtJoueur(id, this.getLoginConnecte());

        return StationSpatialeResponse.convert(station, this.moduleService);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityCreatedResponse create(@Valid @RequestBody CreateOrUpdateStationSpatialeRequest request) {

        return new EntityCreatedResponse(this.service.save(request, this.getLoginConnecte()).getId());

    }
        // Bouton "Passer le tuto" : débloque gratuitement les 3 premiers modules
    @PostMapping("/{id}/debloquer-tuto")
    public StationSpatialeResponse debloquerTuto(@PathVariable Integer id) {

        StationSpatiale station = this.service.debloquerModulesTuto(id, this.getLoginConnecte());

        return StationSpatialeResponse.convert(station, this.moduleService);
    }

    // Bouton "Cycle suivant" : refusé (400) si un déblocage de module est obligatoire
    @PostMapping("/{id}/cycle-suivant")
    public StationSpatialeResponse cycleSuivant(@PathVariable Integer id) {

        StationSpatiale station = this.service.cycleSuivant(id, this.getLoginConnecte());

        return StationSpatialeResponse.convert(station, this.moduleService);
    }

    @PutMapping("/{id}")
    public EntityUpdatedResponse update(@PathVariable Integer id, @Valid @RequestBody CreateOrUpdateStationSpatialeRequest request) {

        this.service.save(id, request);
        return new EntityUpdatedResponse(id, true);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }

    private String getLoginConnecte() { // Permet d'authentifier le login du joueur

        return SecurityContextHolder.getContext().getAuthentication().getName();

    }
}