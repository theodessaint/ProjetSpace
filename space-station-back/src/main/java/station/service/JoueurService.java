package station.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import station.model.Joueur;
import station.repo.JoueurRepository;

@Service
@Transactional
public class JoueurService {
    private static final Logger log = LoggerFactory.getLogger(JoueurService.class);
    private final JoueurRepository repository;

    public JoueurService(JoueurRepository repository) {
        this.repository = repository;
    }

    public List<Joueur> findAll() {
        log.debug("Liste des joueurs ...");

        return this.repository.findAll();
    }

    public Joueur findById(Integer id) {
        log.debug("Recherche du joueur {} ...", id);

        return this.repository.findById(id).orElseThrow(JoueurNotFoundException::new);
    }

    public Joueur save(CreateOrUpdateJoueurRequest request) {
        log.debug("Création d'un joueur ...");

        Joueur joueur = this.save(new Joueur(), request);

        log.debug("Joueur {} ajouté !", joueur.getId());

        return joueur;
    }

    public Joueur save(Integer id, CreateOrUpdateJoueurRequest request) {
        log.debug("Modification du joueur {} ...", id);

        Joueur joueur = this.findById(id);

        joueur = this.save(joueur, request);

        log.debug("Joueur {} modifié !", id);

        return joueur;
    }

    public void deleteById(Integer id) {
        log.debug("Suppression du joueur {} ...", id);

        this.repository.deleteById(id);

        log.debug("Joueur {} supprimé !", id);
    }

    private Joueur save(Joueur joueur, CreateOrUpdateJoueurRequest request) {
        joueur.setId(request.getId());
        joueur.setLogin(request.getLogin());
        joueur.setPassword(request.getPassword());

        return this.repository.save(joueur);
    }
    
}
