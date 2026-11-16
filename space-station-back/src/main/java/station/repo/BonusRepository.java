package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import station.model.Bonus;

public interface BonusRepository extends JpaRepository<Bonus, Integer> {
}