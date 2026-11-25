package station.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import station.api.request.AffecterAstronauteRequest;
import station.api.request.CreateOrUpdateAstronauteRequest;
import station.api.response.AstronauteResponse;
import station.api.response.EntityCreatedResponse;
import station.api.response.EntityUpdatedResponse;
import station.enumerator.RoleEnum;
import station.service.AstronauteService;

@RestController
@RequestMapping("/astronaute")
public class AstronauteApiController {
    private final AstronauteService service;

    public AstronauteApiController(AstronauteService service) {
        this.service = service;
    }

    @GetMapping
    public List<AstronauteResponse> findAll(@Valid @RequestParam(required = false) RoleEnum role) {
        if (role == null) {
            return this.service.findAll().stream().map(AstronauteResponse::convert).toList();
        }

        return this.service.findByRole(role).stream().map(AstronauteResponse::convert).toList();
    }

    @GetMapping("/{id}")
    public AstronauteResponse findById(@PathVariable Integer id) {
        return AstronauteResponse.convert(this.service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityCreatedResponse create(@Valid @RequestBody CreateOrUpdateAstronauteRequest request) {
        return new EntityCreatedResponse(this.service.save(request).getId());
    }

    @PutMapping("/{id}")
    public EntityUpdatedResponse update(@PathVariable Integer id, @Valid @RequestBody CreateOrUpdateAstronauteRequest request) {
        this.service.save(id, request);
        return new EntityUpdatedResponse(id, true);
    }

    @PutMapping("/{id}/affecter")
    public AstronauteResponse affecter (@PathVariable Integer id, @Valid @RequestBody AffecterAstronauteRequest request){
        return AstronauteResponse.convert(this.service.affecter(id, request.getModuleId()));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }
}