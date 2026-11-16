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

import station.model.Ressource;
import station.service.RessourceService;

@RestController
@RequestMapping("/ressource")
public class RessourceApiController {
    private final RessourceService service;

    public RessourceApiController(RessourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ressource> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public Ressource findById(@PathVariable Integer id) {
        return this.service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ressource create(@RequestBody Ressource ressource) {
        return this.service.save(ressource);
    }

    @PutMapping("/{id}")
    public Ressource update(@PathVariable Integer id, @RequestBody Ressource ressource) {
        return this.service.save(id, ressource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }
}