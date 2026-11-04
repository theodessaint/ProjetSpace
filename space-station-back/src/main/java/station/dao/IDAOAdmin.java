package station.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import station.model.Admin;

public interface IDAOAdmin extends JpaRepository<Admin, Integer>{

}