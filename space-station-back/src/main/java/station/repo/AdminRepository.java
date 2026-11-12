package station.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import station.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer>{

    public Optional<Admin> findByLogin(String login);

}