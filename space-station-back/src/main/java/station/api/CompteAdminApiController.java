package station.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import station.model.Joueur;
import station.repo.CompteRepository;
import station.repo.StationSpatialeRepository;

@RestController
@RequestMapping("/admin/joueurs")
public class CompteAdminApiController {

    public record JoueurResponse(Integer id, String login) {}

    private final CompteRepository compteRepository;
    private final StationSpatialeRepository stationRepository;

    public CompteAdminApiController(CompteRepository compteRepository,
                                    StationSpatialeRepository stationRepository) {
        this.compteRepository = compteRepository;
        this.stationRepository = stationRepository;
    }

    // Liste des joueurs (jamais les admins, jamais les mots de passe)
    @GetMapping
    public List<JoueurResponse> findAll() {
        return this.compteRepository.findAllJoueur().stream()
            .map(j -> new JoueurResponse(j.getId(), j.getLogin()))
            .toList();
    }

    // Supprime un joueur et toutes ses parties
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void supprimer(@PathVariable Integer id) {
        Joueur joueur = this.compteRepository.findJoueurById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Joueur introuvable"));

        // d'abord les stations (joueur_id est non nul), avec leurs modules et astronautes
        this.stationRepository.deleteAll(this.stationRepository.findByJoueurId(id));

        this.compteRepository.delete(joueur);
    }
}
