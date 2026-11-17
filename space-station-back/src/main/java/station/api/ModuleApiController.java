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
import station.api.request.CreateOrUpdateModuleRequest;
import station.api.response.EntityCreatedResponse;
import station.api.response.EntityUpdatedResponse;
import station.api.response.ModuleResponse;
import station.service.ModuleService;

@RestController
@RequestMapping("/module")
public class ModuleApiController {
    private final ModuleService service;

    public ModuleApiController(ModuleService service) {
        this.service = service;
    }

    @GetMapping
    public List<ModuleResponse> findAll() {
        return this.service.findAll().stream().map(ModuleResponse::convert).toList();
    }

    @GetMapping("/{id}")
    public ModuleResponse findById(@PathVariable Integer id) {
        return ModuleResponse.convert(this.service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityCreatedResponse create(@Valid @RequestBody CreateOrUpdateModuleRequest request) {
        return new EntityCreatedResponse(this.service.save(request).getId());
    }

    @PutMapping("/{id}")
    public EntityUpdatedResponse update(@PathVariable Integer id, @Valid @RequestBody CreateOrUpdateModuleRequest request) {
        this.service.save(id, request);
        return new EntityUpdatedResponse(id, true);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }
}