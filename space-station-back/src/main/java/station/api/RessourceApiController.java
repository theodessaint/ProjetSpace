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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import station.api.request.CreateOrUpdateRessourceRequest;
import station.api.response.EntityCreatedResponse;
import station.api.response.EntityUpdatedResponse;
import station.api.response.RessourceResponse;
import station.service.RessourceService;

@RestController
@RequestMapping("/ressource")
public class RessourceApiController {
    private final RessourceService service;

    public RessourceApiController(RessourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<RessourceResponse> findAll() {
        return this.service.findAll().stream().map(RessourceResponse::convert).toList();
    }

    @GetMapping("/{id}")
    public RessourceResponse findById(@PathVariable Integer id) {
        return RessourceResponse.convert(this.service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityCreatedResponse create(@Valid @RequestBody CreateOrUpdateRessourceRequest request){
        return new EntityCreatedResponse(this.service.save(request).getId());
    }

    @PutMapping("/{id}")
    public EntityUpdatedResponse update(@PathVariable Integer id, @Valid @RequestBody CreateOrUpdateRessourceRequest request) {
        this.service.save(id, request);
        return new EntityUpdatedResponse(id, true);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }
}