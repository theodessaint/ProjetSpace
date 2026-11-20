package station.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import station.api.request.AuthOrSubscriptionRequest;
import station.api.request.CreateOrUpdateCompteRequest;
import station.api.response.AuthResponse;
import station.api.response.CompteResponse;
import station.api.response.EntityCreatedResponse;
import station.api.response.EntityUpdatedResponse;
import station.model.Joueur;
import station.repo.CompteRepository;
import station.security.service.SecurityService;
import station.service.CompteService;
import org.springframework.web.bind.annotation.ResponseStatus;




@RestController
@RequestMapping("/compte")
public class CompteApiController {
    private final CompteRepository repository;
    private final CompteService service;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    
    private final static  Logger log = LoggerFactory.getLogger(CompteApiController.class);
    
    public CompteApiController(
        CompteRepository repository,
        CompteService service,
        SecurityService securityService, 
        PasswordEncoder passwordEncoder) {
            this.repository = repository;
            this.service = service;
            this.securityService = securityService;
            this.passwordEncoder = passwordEncoder;
        }
        
        @PostMapping("/inscription")
        public EntityCreatedResponse subscribe(@Valid @RequestBody AuthOrSubscriptionRequest request) {
            //L'inscription crée seulement un compte Joueur()
            Joueur joueur = new Joueur();
            
            joueur.setLogin(request.username());
            joueur.setPassword(this.passwordEncoder.encode(request.password()));
            
            this.repository.save(joueur);
            
            return new EntityCreatedResponse(joueur.getId());
        }
        
        @PostMapping("/auth")
        public AuthResponse auth(@Valid @RequestBody AuthOrSubscriptionRequest request) {
            log.debug("Authentification ...");
            
            return this.securityService.auth(request);
        }
        
        @GetMapping()
        public List<CompteResponse> findAll() {
            return this.service.findAll().stream().map(CompteResponse::convert).toList();
        }
        
        @GetMapping("/{id}")
        public CompteResponse findById(@PathVariable Integer id) {
            return CompteResponse.convert(this.service.findById(id));
        }
        
        
        @GetMapping("/{username}")
        public CompteResponse findByUsername(@PathVariable String username) {
            return CompteResponse.convert(this.service.findByUsername(username));
        }
        
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public EntityCreatedResponse create(@Valid @RequestBody CreateOrUpdateCompteRequest request) {
            return new EntityCreatedResponse(this.service.save(request).getId());
        }
        
        @PutMapping("/{id}")
        public EntityUpdatedResponse update(@PathVariable Integer id, @Valid @RequestBody CreateOrUpdateCompteRequest request) {
            this.service.save(id, request);
            return new EntityUpdatedResponse(id, true);
        }
        
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteById(@PathVariable Integer id) {
            this.service.deleteById(id);
        }
        
    }
    