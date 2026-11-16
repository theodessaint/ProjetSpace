package station.api;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import station.api.request.SubscriptionRequest;
import station.api.response.EntityCreatedResponse;
import station.model.Compte;
import station.repo.CompteRepository;

@RestController
@RequestMapping("/compte")
public class CompteApiController {
    private final CompteRepository repository;
    private final PasswordEncoder passwordEncoder;
    
    public CompteApiController(CompteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/inscription")
    public EntityCreatedResponse subscribe(@Valid @RequestBody SubscriptionRequest request) {
        Compte compte = new Compte();
        
        compte.setLogin(request.getUsername());
        compte.setPassword(this.passwordEncoder.encode(request.getPassword()));
        
        this.repository.save(compte);
        
        return new EntityCreatedResponse(compte.getId());
    }
}
