package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import station.model.AstronauteReference;

public interface AstronauteReferenceRepository extends JpaRepository<AstronauteReference, Integer> {

}
