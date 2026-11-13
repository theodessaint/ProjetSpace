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
import station.api.request.CreateOrUpdateAstronauteRequest;
import station.api.response.AstronauteResponse;
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
    public List<AstronauteResponse> findAll(@RequestParam(required = false) RoleEnum role) {
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
    public AstronauteResponse create(@Valid @RequestBody CreateOrUpdateAstronauteRequest request) {
        return AstronauteResponse.convert(this.service.save(request));
    }

    @PutMapping("/{id}")
    public AstronauteResponse update(@PathVariable Integer id, @Valid @RequestBody CreateOrUpdateAstronauteRequest request) {
        return AstronauteResponse.convert(this.service.save(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }
}