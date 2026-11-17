package station.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import station.model.Compte;

public interface CompteRepository extends JpaRepository<Compte, Integer>{
   
    public Optional<Compte> findById(Integer id);

    public Optional<Compte> findByLogin(String username);

    // findByRole ?
    
   // public List<Compte> findAllJoueur();

  //  public List<Compte> findAllAdmin();

}
