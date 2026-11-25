package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import station.enumerator.ModuleEnum;
import station.enumerator.RessourceEnum;
import station.enumerator.RoleEnum;
import station.model.Bonus;

public interface BonusRepository extends JpaRepository<Bonus, Integer> {
    boolean existsByRoleAndModuleAndRessource(
    RoleEnum role,
    ModuleEnum module,
    RessourceEnum ressource
);
}