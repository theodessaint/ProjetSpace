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
import station.api.request.CreateOrUpdateStationSpatialeRequest;
import station.api.response.EntityCreatedResponse;
import station.api.response.EntityUpdatedResponse;
import station.model.StationSpatiale;
import station.service.StationSpatialeService;

@RestController
@RequestMapping("/station")
public class StationSpatialeApiController {
    private final StationSpatialeService service;

    public StationSpatialeApiController(StationSpatialeService service) {
        this.service = service;
    }

    @GetMapping
    public List<StationSpatiale> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public StationSpatiale findById(@PathVariable Integer id) {
        return this.service.findById(id);
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


/* 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StationSpatiale create(@RequestBody StationSpatiale station) {
        return this.service.save(station);
    }

    @PutMapping("/{id}")
    public StationSpatiale update(@PathVariable Integer id, @RequestBody StationSpatiale station) {
        return this.service.save(id, station);
    }
 */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }
}