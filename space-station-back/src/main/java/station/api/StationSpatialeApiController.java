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
import station.model.StationSpatiale;
import station.service.ModuleService;
import station.service.StationSpatialeService;
import org.springframework.web.bind.annotation.RequestParam;


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
        .map(station -> StationSpatialeResponse.convert(station, this.moduleService.peutDebloquer(station)))
        .toList();
    }
    
    @GetMapping("/ma-partie") // Renvoie la partie du joueur connecté
    public StationSpatialeResponse findMaPartie() {
        
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        StationSpatiale station = this.service.findByJoueurId(login);
        return StationSpatialeResponse.convert(station, this.moduleService.peutDebloquer(station));

    }
    
    @GetMapping("/{id}")
    public StationSpatialeResponse findById(@PathVariable Integer id) {
        StationSpatiale station = this.service.findById(id);
        return StationSpatialeResponse.convert(station, this.moduleService.peutDebloquer(station));
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityCreatedResponse create(@Valid @RequestBody CreateOrUpdateStationSpatialeRequest request) {
        
        return new EntityCreatedResponse(this.service.save(request).getId());
        
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
}