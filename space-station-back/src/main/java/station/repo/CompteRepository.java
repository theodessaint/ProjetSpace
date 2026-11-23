package station.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import station.model.Admin;
import station.model.Compte;
import station.model.Joueur;

public interface CompteRepository extends JpaRepository<Compte, Integer>{
  
  public Optional<Compte> findById(Integer id);
  
  public Optional<Compte> findByLogin(String username);
  
  // findByRole ?

  @Query ("SELECT j from Joueur j WHERE j.id = :id")
  Optional<Joueur> findJoueurById(Integer id);

  @Query ("SELECT j from Joueur j WHERE j.login = :login")
  Optional<Joueur> findJoueurByLogin(String login);
  
  @Query("SELECT j FROM Joueur j")
  List<Joueur> findAllJoueur();
  
  @Query("SELECT a FROM Admin a")
  List<Admin> findAllAdmin();  
}
