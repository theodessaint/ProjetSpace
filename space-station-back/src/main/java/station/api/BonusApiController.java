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

import station.model.Bonus;
import station.service.BonusService;

@RestController
@RequestMapping("/bonus")
public class BonusApiController {
    private final BonusService service;

    public BonusApiController(BonusService service) {
        this.service = service;
    }

    @GetMapping
    public List<Bonus> findAll() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public Bonus findById(@PathVariable Integer id) {
        return this.service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bonus create(@RequestBody Bonus bonus) {
        return this.service.save(bonus);
    }

    @PutMapping("/{id}")
    public Bonus update(@PathVariable Integer id, @RequestBody Bonus bonus) {
        return this.service.save(id, bonus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Integer id) {
        this.service.deleteById(id);
    }
}