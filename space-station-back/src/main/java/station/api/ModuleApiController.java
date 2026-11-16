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

import station.model.Module;
import station.service.ModuleService;

@RestController
@RequestMapping("/module")
public class ModuleApiController {
    private final ModuleService service;

    public ModuleApiController(ModuleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Module> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public Module findById(@PathVariable Integer id) {
        return this.service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Module create(@RequestBody Module module) {
        return this.service.save(module);
    }

    @PutMapping("/{id}")
    public Module update(@PathVariable Integer id, @RequestBody Module module) {
        return this.service.save(id, module);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }
}